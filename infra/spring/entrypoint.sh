#!/bin/sh

cd /app

if [ ! -f app.jar ]; then
  echo "Downloading latest app.jar..."
  curl -sSL "$APP_JAR_URL" -o app.jar
fi

echo "Starting application..."
exec java -jar app.jar
