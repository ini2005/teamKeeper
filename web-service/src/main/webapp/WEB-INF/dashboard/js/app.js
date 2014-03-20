'use strict';
var amsApplication;

bindAppliction();
bindServices();
bindViewModels();
bindRoutingConfiguration();
bindDirectives();

/**
 * Bind the applications
 */
function bindAppliction() {
	// Declare app level module which depends on filters, and services
	amsApplication = angular.module("keeper", [ 'ui.router', 'ui.bootstrap' ]);
}

/**
 * Bind the services
 */
function bindServices() {
	amsApplication.service('mainService', [ '$log', '$http', mainService ]);
}

/**
 * Bind the view models
 */
function bindViewModels() {
	amsApplication.controller('mainViewModel', [ '$scope', '$log', '$state',
			'$stateParams', 'mainService', mainViewModel ]);
}

/**
 * Bind the routing configuration
 */
function bindRoutingConfiguration() {

	amsApplication.config([ '$stateProvider', '$urlRouterProvider',
			function($stateProvider, $urlRouterProvider) {

				$urlRouterProvider.otherwise('main/');

				$stateProvider.state("main", {

					url : '/main/',
					views : {

						'main-view@' : {
							templateUrl : 'views/main.html',
							controller : 'mainViewModel'
						}
					}

				});

			} ]);

}

/**
 * bind directives
 */
function bindDirectives() {

	amsApplication.directive('errSrc', function() {
		return {
			link : function(scope, element, attrs) {
				element.bind('error', function() {
					element.attr('src', attrs.errSrc);
				});
			}
		};
	});

	amsApplication.directive('ngEnter', function() {
		return function(scope, element, attrs) {
			element.bind("keydown keypress", function(event) {
				if (event.which === 13) {
					scope.$apply(function() {
						scope.$eval(attrs.ngEnter, {
							'event' : event
						});
					});

					event.preventDefault();
				}
			});
		};
	});

}
