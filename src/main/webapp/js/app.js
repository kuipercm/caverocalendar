'use strict';

var caveroSocialCalendar = angular.module('caveroSocialCalendar', ['restangular', 'ui.calendar', 'ui.bootstrap', 'ui.bootstrap.datetimepicker', 'caveroScControllers']);

caveroSocialCalendar.config(function(RestangularProvider) {
    RestangularProvider.setBaseUrl('/calendar/resources')
});