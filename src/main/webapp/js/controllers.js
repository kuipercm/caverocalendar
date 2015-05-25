'use strict';

angular.module('caveroScControllers', [])
    .controller('calendarController', ['$scope', 'Restangular', 'uiCalendarConfig', function($scope, Restangular, uiCalendarConfig) {
        var self = this;
        var eventEndpoint = Restangular.all('event');

        self.selectedactivity = {};

        self.events = {
            url: 'resources/event',
            currentTimezone: 'Europe/Amsterdam'
        };

        self.renderEvent = function(event, element) {
            element.attr('href', 'javascript:void(0);');
            element.attr('data-ng-click', 'ctrl.handleEventClick()');
        };

        self.handleEventClick = function(event) {
            var eventId = event.id;

            eventEndpoint.one('id', eventId).get().then(function(singleEventData) {
                self.selectedactivity = singleEventData;
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
            var start = new Date();
            start.setHours(start.getHours() + 1);
            start.setMinutes(0);
            start.setSeconds(0);
            start.setMilliseconds(0);

            var end = new Date();
            end.setTime(start.getTime());
            end.setHours(end.getHours() + 1);

            self.selectedactivity = {
                start: start,
                end: end
            };
        };
        self.closeActivity = function() {
            self.selectedactivity = {};
        };
        self.submitActivity = function() {
            if (self.selectedactivity.id) {
                self.selectedactivity.put().then(function() {
                    self.reloadCalendar();
                });
            }
            else {
                eventEndpoint.post(self.selectedactivity).then(function() {
                    self.reloadCalendar();
                });
            }
        };
        self.deleteActivity = function() {
            if (self.selectedactivity.id) {
                self.selectedactivity.remove().then(function() {
                    self.reloadCalendar();
                });
            }
            else {
                self.resetSelection();
            }

        };
        self.reloadCalendar = function() {
            uiCalendarConfig.calendars.caveroCalendar.fullCalendar('refetchEvents');
            self.resetSelection();
        };
        self.resetSelection = function() {
            self.selectedactivity = {};
        };
    }]);