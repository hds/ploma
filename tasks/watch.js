'use strict';

var gulp = require('gulp');
var watch = require('gulp-watch');
var sass = require('gulp-sass');

gulp.task('watch', ['sass'], function() {
    return watch('app/dev/sass/*.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest('app/dev/css'));
});

gulp.task('sass', function () {
    return gulp.src('app/dev/sass/*.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest('app/dev/css'));
});
