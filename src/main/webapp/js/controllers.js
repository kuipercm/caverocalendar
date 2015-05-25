'use strict';

angular.module('caveroScControllers', [])
    .controller('calendarController', ['$scope', 'Restangular', 'uiCalendarConfig', function($scope, Restangular, uiCalendarConfig) {
        var self = this;
        var eventEndpoint = Restangular.all('event');

        self.selectedactivity = {};
        self.newactivity = false;

        self.events = {
            url: 'resources/event',
            currentTimezone: 'Europe/Amsterdam'
        };

        self.renderEvent = function(event, element) {
            element.attr('href', 'javascript:void(0);');
            element.attr('data-ng-click', 'ctrl.handleEventClick()');
        };

        self.handleEventClick = function(event) {
            console.log('calling the handle click function');
            var eventId = event.id;

            eventEndpoint.one('id', eventId).get().then(function(singleEventData) {
                console.log('success callback from rest call for event');
                self.selectedactivity = singleEventData;
                self.newactivity = false;
            });

            //returning false so the default window.open doesn't get called
            return false;
        };

        self.caveroConfig = {
            eventRender: self.renderEvent,
            eventClick: self.handleEventClick,
            timeFormat: 'H(:mm)'
        };

        self.createNewActivity = function() {
            self.newactivity = true;
            self.selectedactivity = {
                start: new Date(),
                end: new Date()
            };
        };
        self.abortNewActivity = function() {
            self.newactivity = false;
            self.selectedactivity = {};
        };
        self.submitNewActivity = function() {
            console.log('posting: ' + self.selectedactivity);

            eventEndpoint.post(self.selectedactivity).then(function() {
                uiCalendarConfig.calendars.caveroCalendar.fullCalendar('refetchEvents');
                self.newactivity = false;
                self.selectedactivity = {};
            });
        }
    }]);