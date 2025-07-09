document.addEventListener('DOMContentLoaded', function () {
    const typesHelpIcon = document.getElementById('types-help-icon');
    if (typesHelpIcon) {
        const popoverContent = `
            <ul class="list-unstyled">
                <li><b>Public:</b> 공휴일</li>
                <li><b>Bank:</b> 은행·관공서 휴무</li>
                <li><b>School:</b> 학교 휴교일</li>
                <li><b>Authorities:</b> 관공서 휴무</li>
                <li><b>Optional:</b> 선택적 휴무일 (대부분 휴일)</li>
                <li><b>Observance:</b> 기념일 (유급 휴무 아님)</li>
            </ul>
        `;
        new bootstrap.Popover(typesHelpIcon, {
            title: 'Holiday Types',
            content: popoverContent,
            trigger: 'click',
            placement: 'top',
            html: true
        });
    }
    const scrapeCountriesBtn = document.getElementById('scrapeCountriesBtn');
    const scrapeHolidaysBtn = document.getElementById('scrapeHolidaysBtn');
    const viewHolidaysBtn = document.getElementById('viewHolidaysBtn');
    const deleteHolidaysBtn = document.getElementById('deleteHolidaysBtn');
    const yearScrapeInput = document.getElementById('yearScrapeInput');
    const yearInput = document.getElementById('yearInput');
    const countrySelect = document.getElementById('countrySelect');
    const holidaysTableBody = document.getElementById('holidaysTableBody');
    const prevPageItem = document.getElementById('prev-page-item');
    const nextPageItem = document.getElementById('next-page-item');
    const pageInfoItem = document.getElementById('page-info-item');

    // New elements for country selection
    const availableCountriesSelect = document.getElementById('availableCountriesSelect');
    const selectedCountriesSelect = document.getElementById('selectedCountriesSelect');
    const addCountryBtn = document.getElementById('addCountryBtn');
    const removeCountryBtn = document.getElementById('removeCountryBtn');
    const filterAvailableInput = document.getElementById('filterAvailableInput'); // Get the filter input

    let allAvailableCountries = [];
    let currentPage = 0;
    let totalPages = 0;

    // Load countries on page load
    loadCountries();

    function loadCountries() {
        fetch('/api/countries')
            .then(response => response.json())
            .then(data => {
                if (data && data.data && data.data.length > 0) {
                    allAvailableCountries = data.data;
                    countrySelect.innerHTML = '';
                    availableCountriesSelect.innerHTML = '';

                    const allOption = document.createElement('option');
                    allOption.value = 'ALL';
                    allOption.textContent = 'All Countries';
                    countrySelect.appendChild(allOption);

                    let krExists = false;
                    allAvailableCountries.forEach(country => {
                        const option = document.createElement('option');
                        option.value = country.code;
                        option.textContent = `${country.name} (${country.code})`;
                        countrySelect.appendChild(option.cloneNode(true));
                        availableCountriesSelect.appendChild(option);

                        if (country.code === 'KR') {
                            krExists = true;
                        }
                    });

                    if (krExists) {
                        countrySelect.value = 'KR';
                    }
                } else {
                    const noDataMessage = '수집된 국가 목록이 없습니다. <br>국가 정보를 먼저 수집 후 작업해주세요.';
                    countrySelect.innerHTML = `<option disabled>${noDataMessage}</option>`;
                    availableCountriesSelect.innerHTML = `<option disabled>${noDataMessage}</option>`;
                }
            })
            .catch(error => {
                console.error('Error loading countries:', error)
                countrySelect.innerHTML = '<option>Failed to load</option>';
                availableCountriesSelect.innerHTML = '<option>Failed to load</option>';
            });
    }

    function viewHolidays(page = 0) {
        const year = yearInput.value;
        const countryCode = countrySelect.value;
        const size = 10;

        if (!year) {
            alert('Please select a year.');
            return;
        }

        let fetchUrl = `/api/holidays/year/${year}?page=${page}&size=${size}`;
        if (countryCode && countryCode !== 'ALL') {
            fetchUrl = `/api/holidays/year/${year}/country/${countryCode}?page=${page}&size=${size}`;
        }

        fetch(fetchUrl)
            .then(response => response.json())
            .then(data => {
                holidaysTableBody.innerHTML = '';
                if (data && data.data && data.data.content && data.data.content.length > 0) {
                    const pageData = data.data;
                    currentPage = pageData.page;
                    totalPages = pageData.totalPages;

                    pageData.content.forEach(holiday => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${holiday.countryCode}</td>
                            <td>${holiday.date}</td>
                            <td>${holiday.types ? (Array.isArray(holiday.types) ? holiday.types.join(', ') : holiday.types) : ''}</td>
                            <td>${holiday.localName}</td>
                            <td>${holiday.name}</td>
                            <td>${holiday.counties || ''}</td>
                        `;
                        holidaysTableBody.appendChild(row);
                    });

                    updatePaginationControls();
                } else {
                    holidaysTableBody.innerHTML = `<td colspan="6" class="text-center">조건과 일치하는 공휴일 정보가 없습니다.</td>`;
                    totalPages = 0;
                    currentPage = 0;
                    updatePaginationControls();
                }
            })
            .catch(error => {
                console.error('Error fetching holidays:', error);
                holidaysTableBody.innerHTML = `<td colspan="6" class="text-center text-danger">API 호출에 실패했습니다.</td>`;
                totalPages = 0;
                currentPage = 0;
                updatePaginationControls();
            });
    }

    function updatePaginationControls() {
        pageInfoItem.querySelector('.page-link').textContent = `Page ${currentPage + 1} of ${totalPages}`;

        if (currentPage === 0) {
            prevPageItem.classList.add('disabled');
        } else {
            prevPageItem.classList.remove('disabled');
        }

        if (currentPage >= totalPages - 1) {
            nextPageItem.classList.add('disabled');
        } else {
            nextPageItem.classList.remove('disabled');
        }

        if (totalPages === 0) {
            prevPageItem.classList.add('disabled');
            nextPageItem.classList.add('disabled');
            pageInfoItem.querySelector('.page-link').textContent = `Page 1 of 1`;
        }
    }

    function scrapeCountries() {
        fetch('/api/scraper/countries', { method: 'POST' })
            .then(response => response.json())
            .then(data => {
                alert(data.message);
                setTimeout(() => {
                    window.location.reload();
                }, 5000);
            })
            .catch(error => {
                alert('국가 수집 작업 요청을 실패했습니다.');
            });
    }

    function scrapeHolidays() {
        const year = yearScrapeInput.value;
        if (!year) {
            alert('스크랩 하고자하는 연도를 입력해주세요.');
            return;
        }

        const selectedCountries = Array.from(selectedCountriesSelect.options).map(opt => opt.value);
        if (selectedCountries.length === 0) {
            alert('적어도 1개 이상의 국가를 선택해주세요.');
            return;
        }

        const body = {
            year: parseInt(year),
            countries: selectedCountries
        };

        fetch('/api/scraper/holidays', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        })
        .then(response => response.json())
        .then(data => {
            alert(`선택된 국가의 ${year}년 공휴일 수집 요청을 성공적으로 전송했습니다.`);
        })
        .catch(error => {
            alert(`공휴일 수집 요청 전송에 실패했습니다.`);
        });
    }

    function moveOptions(source, destination) {
        Array.from(source.selectedOptions).forEach(option => {
            destination.appendChild(option);
        });
    }

    function filterAvailableCountries() {
        const filterText = filterAvailableInput.value.toLowerCase();
        availableCountriesSelect.innerHTML = '';

        allAvailableCountries.filter(country => {
            return country.name.toLowerCase().includes(filterText) ||
                   country.countryCode.toLowerCase().includes(filterText);
        }).forEach(country => {
            const option = document.createElement('option');
            option.value = country.countryCode;
            option.textContent = `${country.name} (${country.countryCode})`;
            availableCountriesSelect.appendChild(option);
        });
    }

    // Event Listeners
    scrapeCountriesBtn.addEventListener('click', scrapeCountries);
    scrapeHolidaysBtn.addEventListener('click', scrapeHolidays);
    viewHolidaysBtn.addEventListener('click', () => viewHolidays(0));
    deleteHolidaysBtn.addEventListener('click', deleteHolidays);

    addCountryBtn.addEventListener('click', () => moveOptions(availableCountriesSelect, selectedCountriesSelect));
    removeCountryBtn.addEventListener('click', () => moveOptions(selectedCountriesSelect, availableCountriesSelect));

    filterAvailableInput.addEventListener('input', filterAvailableCountries); // Add event listener for filtering

    prevPageItem.addEventListener('click', (e) => {
        e.preventDefault();
        if (!prevPageItem.classList.contains('disabled')) {
            viewHolidays(currentPage - 1);
        }
    });

    nextPageItem.addEventListener('click', (e) => {
        e.preventDefault();
        if (!nextPageItem.classList.contains('disabled')) {
            viewHolidays(currentPage + 1);
        }
    });

    function deleteHolidays() {
        const year = yearInput.value;
        const countryCode = countrySelect.value;

        if (!year) {
            alert('삭제할 공휴일의 연도를 입력해주세요.');
            return;
        }

        if (countryCode === 'ALL') {
            alert('모든 국가의 공휴일을 삭제할 수 없습니다. 특정 국가를 선택해주세요.');
            return;
        }

        if (!confirm(`정말로 ${year}년 ${countryCode} 국가의 공휴일을 삭제하시겠습니까?`)) {
            return;
        }

        const deleteUrl = `/api/holidays?countryCode=${countryCode}&year=${year}`;

        fetch(deleteUrl, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    return response.json().then(data => {
                        alert('성공적으로 삭제되었습니다.');
                        viewHolidays(0);
                    });
                } else {
                    return response.json().then(err => {
                        alert('삭제 실패');
                    });
                }
            })
            .catch(error => {
                console.error('공휴일 삭제 오류: ', error);
                alert('공휴일 삭제 중 오류가 발생했습니다.');
            });
    }
});
