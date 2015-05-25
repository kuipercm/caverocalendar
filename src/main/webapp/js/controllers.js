'use strict';

angular.module('caveroScControllers', [])
    .controller('calendarController', ['$scope', function($scope) {
        var self = this;

        self.events = {
            url: 'data/social_events.json',
            currentTimezone: 'Europe/Amsterdam'
        };
    }]);