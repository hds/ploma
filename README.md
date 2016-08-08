# Ploma

A simple, cross-platform SQLite browser.

Very much a work in progress.

## Setup instructions

This project is based on the [descjop]() leiningen template. To run it after cloning, enter the following commands:

    lein descjop-init
    lein descjop-externs
    lein descjop-once

**Note**: You will get an error about missing Gruntfile or missing local Grunt installation after running `lein descjop-init`. This is because I'm using Gulp in favour of Grunt for the time being.

## Running (for development)

Here's the horrible part. For development, you need to be running Figwheel, a Gulp watch task for Sass and the Electron app itself. You'll want three separate terminal windows open and then run:

    # Terminal 1 (SCSS to CSS conversion)
    gulp watch

    # Terminal 2 (ClojureScript compilation)
    lein figwheel

    # Terminal 3 (Electron app)
    npm start

It would be nice to make this easier in the future.

**Note**: Ploma currently expects an SQLite database called test.sqlite to be found in the project directory, it won't start up otherwise.
