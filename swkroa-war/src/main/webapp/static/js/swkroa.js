/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

// add a 'startsWith' method to the String class
if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function(str) {
    return this.slice(0, str.length) === str;
  };
}

(function(window, angular) {
  'use strict';

  angular.module('swkroaApp',
    ['ui.bootstrap',
     'ui.utils',
     'ui.router',
     'xeditable']
  );

  angular.module('swkroaApp').filter('tel', function () {
    return function (tel) {
      if (!tel) { return ''; }

      var value = tel.toString().trim().replace(/^\+/, '');

      if (value.match(/[^0-9]/)) {
        return tel;
      }

      var country, city, number;

      switch (value.length) {
        case 10: // +1PPP####### -> C (PPP) ###-####
          country = 1;
          city = value.slice(0, 3);
          number = value.slice(3);
          break;

        case 11: // +CPPP####### -> CCC (PP) ###-####
          country = value[0];
          city = value.slice(1, 4);
          number = value.slice(4);
          break;

        case 12: // +CCCPP####### -> CCC (PP) ###-####
          country = value.slice(0, 3);
          city = value.slice(3, 5);
          number = value.slice(5);
          break;

        default:
          return tel;
      }

      if (country == 1) {
        country = "";
      }

      number = number.slice(0, 3) + '-' + number.slice(3);

      return (country + " (" + city + ") " + number).trim();
    };
  });

  angular.module('swkroaApp').filter('zip', function () {
    return function (zip) {
      if (!zip) { return ''; }

      var value = zip.toString().trim().replace(/^\+/, '');

      if (value.match(/[^0-9]/)) {
        return zip;
      }

      switch (value.length) {
        case 9: // ######### -> #####-####
          zip = value.slice(0, 5);
          return zip + "-" + value.slice(5);
          break;

        default:
          return zip;
      }
    };
  });

  angular.module('swkroaApp').config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('contextRootInterceptor');
  }]);

  angular.module('swkroaApp').config(['uibDatepickerConfig', function(uibDatepickerConfig) {
    uibDatepickerConfig.showWeeks = false;
  }]);

  angular.module('swkroaApp').config(['uibDatepickerPopupConfig', function(uibDatepickerPopupConfig) {
    uibDatepickerPopupConfig.showButtonBar = true;
  }]);

// add an interceptor to our http service to inject the context root for our requests
  angular.module('swkroaApp').factory('contextRootInterceptor', function() {
    return {
      'request': function(config) {
        if (config.url.startsWith("/")) {
          config.url = "/swkroa" + config.url;
        }

        return config;
      },

      'responseError': function(rejection) {
        if (rejection.status == 409) {
          $('#optimisticErrorMessageDlg').modal('show');
        } else if (rejection.status >= 400 && rejection.status <= 599) {
          $('#unknownErrorMessage').text(rejection.data.message);
          $('#unknownErrorMessageDlg').modal('show');
        }

        return rejection;
      }
    };
  });

})(window, window.angular);

responseSuccessful = function(response) {
  return (200 <= response.status && response.status <= 299);
};

showProcessingDialog = function() {
  $('#pleaseWaitDlg').modal('show');
};

hideProcessingDialog = function() {
  $('#pleaseWaitDlg').modal('hide');
};
