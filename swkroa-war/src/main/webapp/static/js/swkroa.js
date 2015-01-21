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

angular.module('ng').filter('tel', function () {
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

angular.module('ng').filter('zip', function () {
  return function (zip) {
    if (!zip) { return ''; }

    var value = zip.toString().trim().replace(/^\+/, '');

    if (value.match(/[^0-9]/)) {
      return zip;
    }

    switch (value.length) {
    case 9: // ######### -> #####-####
      zip = value.slice(0, 5);
      plus4 = value.slice(5);
      return zip + "-" + plus4;
      break;

    default:
      return zip;
    }
  };
});

var swkroaApp = angular.module('swkroaApp',
  ['ui.bootstrap',
   'ui.utils',
   'ui.router',
   'xeditable']
);

responseSuccessful = function(response) {
 return (200 <= response.status && response.status <= 299);
};

// add an interceptor to our http service to inject the context root for our requests
swkroaApp.factory('contextRootInterceptor', function() {
  return {
    'request': function(config) {
      if (config.url.startsWith("/")) {
        config.url = "/swkroa-war" + config.url;
      }

      return config;
    },

    'responseError': function(rejection) {
      if (rejection.status == 409) {
        $('#optimisticErrorMessage').modal('show');
      } else if (rejection.status == 500) {
        $('#unknownErrorMessage').modal('show');
      }

      return rejection;
    }
  };
});

swkroaApp.factory('swkroaCache', function($cacheFactory) {
  return $cacheFactory('swkroaCache');
});

swkroaApp.config(['$httpProvider', function($httpProvider) {
  $httpProvider.interceptors.push('contextRootInterceptor');
}]);

// define a service for CodeSet / CodeValues
swkroaApp.service('codesetService', ['$http', 'swkroaCache', function($http, swkroaCache) {
  this.getCodeSets = function() {
    var promise = $http.get('/api/codesets');

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.getCodeValuesForCodeSet = function(codeSetMeaning) {
    var promise = $http.get('/api/codesets/' + codeSetMeaning);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.saveCodeSet = function(codeSet) {
    var promise = $http.post('/api/codesets', codeset);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.saveCodeValue = function(codeSetMeaning, codeValue) {
    var promise = $http.post('/api/codesets/' + codeSetMeaning, codeValue);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };
}]);

// define a service for Contacts
swkroaApp.service('contactService', ['$http', function($http) {
  this.getStates = function() {
    return [
      {code:"AL", name:"Alabama"},
      {code:"AK", name:"Alaska"},
      {code:"AS", name:"American Samoa"},
      {code:"AZ", name:"Arizona"},
      {code:"AR", name:"Arkansas"},
      {code:"AF", name:"Armed Forces Africa"},
      {code:"AA", name:"Armed Forces Americas"},
      {code:"AC", name:"Armed Forces Canada"},
      {code:"AE", name:"Armed Forces Europe"},
      {code:"AM", name:"Armed Forces Middle East"},
      {code:"AP", name:"Armed Forces Pacific"},
      {code:"CA", name:"California"},
      {code:"CO", name:"Colorado"},
      {code:"CT", name:"Connecticut"},
      {code:"DE", name:"Delaware"},
      {code:"DC", name:"District of Columbia"},
      {code:"FM", name:"Federated States Of Micronesia"},
      {code:"FL", name:"Florida"},
      {code:"GA", name:"Georgia"},
      {code:"GU", name:"Guam"},
      {code:"HI", name:"Hawaii"},
      {code:"ID", name:"Idaho"},
      {code:"IL", name:"Illinois"},
      {code:"IN", name:"Indiana"},
      {code:"IA", name:"Iowa"},
      {code:"KS", name:"Kansas"},
      {code:"KY", name:"Kentucky"},
      {code:"LA", name:"Louisiana"},
      {code:"ME", name:"Maine"},
      {code:"MH", name:"Marshall Islands"},
      {code:"MD", name:"Maryland"},
      {code:"MA", name:"Massachusetts"},
      {code:"MI", name:"Michigan"},
      {code:"MN", name:"Minnesota"},
      {code:"MS", name:"Mississippi"},
      {code:"MO", name:"Missouri"},
      {code:"MT", name:"Montana"},
      {code:"NE", name:"Nebraska"},
      {code:"NV", name:"Nevada"},
      {code:"NH", name:"New Hampshire"},
      {code:"NJ", name:"New Jersey"},
      {code:"NM", name:"New Mexico"},
      {code:"NY", name:"New York"},
      {code:"NC", name:"North Carolina"},
      {code:"ND", name:"North Dakota"},
      {code:"MP", name:"Northern Mariana Islands"},
      {code:"OH", name:"Ohio"},
      {code:"OK", name:"Oklahoma"},
      {code:"OR", name:"Oregon"},
      {code:"PW", name:"Palau"},
      {code:"PA", name:"Pennsylvania"},
      {code:"PR", name:"Puerto Rico"},
      {code:"RI", name:"Rhode Island"},
      {code:"SC", name:"South Carolina"},
      {code:"SD", name:"South Dakota"},
      {code:"TN", name:"Tennessee"},
      {code:"TX", name:"Texas"},
      {code:"UT", name:"Utah"},
      {code:"VT", name:"Vermont"},
      {code:"VI", name:"Virgin Islands"},
      {code:"VA", name:"Virginia"},
      {code:"WA", name:"Washington"},
      {code:"WV", name:"West Virginia"},
      {code:"WI", name:"Wisconsin"},
      {code:"WY", name:"Wyoming"}
    ]
  };

  this.addAddress = function(entity) {
    if (!entity.addresses) {
      entity.addresses = new Array();
    }

    entity.addresses.push({
      active: true
    });
  };

  this.removeAddress = function(entity, address) {
    if (address.addressUID > 0) {
      address.active = false;
    } else {
      var idx = entity.addresses.indexOf(address);
      entity.addresses.splice(idx, 1);
    }
  };

  this.ensurePrimaryAddress = function(entity, address) {
    if (address.primary) {
      var pos = entity.addresses.indexOf(address);

      for (var idx = 0; idx < entity.addresses.length; idx++) {
        if (entity.addresses[idx].primary && pos != idx) {
          entity.addresses[idx].primary = false;
        }
      }
    }
  };

  this.addPhone = function(entity) {
    if (!entity.phoneNumbers) {
      entity.phoneNumbers = new Array();
    }

    entity.phoneNumbers.push({
      active: true
    });
  };

  this.removePhone = function(entity, phone) {
    if (phone.phoneUID > 0) {
      phone.active = false;
    } else {
      var idx = entity.phoneNumbers.indexOf(phone);
      entity.phoneNumbers.splice(idx, 1);
    }
  };

  this.ensurePrimaryPhone = function(entity, phone) {
    if (phone.primary) {
      var pos = entity.phoneNumbers.indexOf(phone);

      for (var idx = 0; idx < entity.phoneNumbers.length; idx++) {
        if (entity.phoneNumbers[idx].primary && pos != idx) {
          entity.phoneNumbers[idx].primary = false;
        }
      }
    }
  };

  this.addEmail = function(entity) {
    if (!entity.emailAddresses) {
      entity.emailAddresses = new Array();
    }
    entity.emailAddresses.push({
      active: true
    });
  };

  this.removeEmail = function(entity, email) {
    if (email.emailAddressUID > 0) {
      email.active = false;
    } else {
      var idx = entity.emailAddresses.indexOf(email);
      entity.emailAddresses.splice(idx, 1);
    }
  };

  this.ensurePrimaryEmail = function(entity, email) {
    if (email.primary) {
      var pos = entity.emailAddresses.indexOf(email);

      for (var idx = 0; idx < entity.emailAddresses.length; idx++) {
        if (entity.emailAddresses[idx].primary && pos != idx) {
          entity.emailAddresses[idx].primary = false;
        }
      }
    }
  };

  this.getAddressTypeDisplay = function(address, types) {
    for (var idx1 = 0; idx1 < types.length; idx1++) {
      if (address.addressTypeCD == types[idx1].codeValueUID) {
        return types[idx1].display;
      }
    }
  };

  this.getPhoneTypeDisplay = function(phone, types) {
    for (var idx1 = 0; idx1 < types.length; idx1++) {
      if (phone.phoneTypeCD == types[idx1].codeValueUID) {
        return types[idx1].display;
      }
    }
  };

  this.getEmailTypeDisplay = function(email, types) {
    for (var idx1 = 0; idx1 < types.length; idx1++) {
      if (email.emailTypeCD == types[idx1].codeValueUID) {
        return types[idx1].display;
      }
    }
  };
}]);

// define a service for Memberships
swkroaApp.service('membershipService', ['$http', function($http) {
  var rootUrl = "/api/memberships";

  this.getMembership = function(membershipUID) {
    var promise = $http.get(rootUrl + '/' + membershipUID);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.getMemberships = function(query, status, balance) {
    var params = "";

    if (query && query.length > 0) {
      params = "?q=" + query;
    }

    if (status && status.length > 0) {
      if (params.length == 0) {
        params = "?";
      } else {
        params = params + "&";
      }

      params = params + "status=" + status;
    }

    if (balance && balance.length > 0) {
      if (params.length == 0) {
        params = "?";
      } else {
        params = params + "&";
      }

      params = params + "balance=" + balance;
    }

    var promise = $http.get(rootUrl + params);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.getDelinquentMemberships = function() {
    var promise = $http.get(rootUrl + "?balance=DELINQUENT");

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.saveMembership = function(membership) {
    var promise = $http.post(rootUrl, membership);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  }

  this.generateOwnerId = function(firstName, lastName) {
    var promise = $http.get(rootUrl + '/ownerId/' + firstName + "/" + lastName).then(function(response) {
      return JSON.parse(response.data);
    });

    return promise;
  };

  this.closeMemberships = function(membershipsArg, closeReasonArg, closeTextArg) {
    var closeReasonText = "";
    if (closeTextArg) {
      closeReasonText = closeTextArg;
    };

    var data = {
      memberships: membershipsArg,
      closeReason: closeReasonArg,
      closeText: closeReasonText
    };

    var promise = $http.post(rootUrl + '/close', JSON.stringify(data));

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.openMemberships = function(membershipsArg) {
    var data = {
      memberships: membershipsArg
    };

    var promise = $http.post(rootUrl + '/open', JSON.stringify(data));

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };
}]);

// define a service for Transactions
swkroaApp.service('transactionService', ['$http', function($http) {
  this.getUnpaidTransactions = function() {
    var promise = $http.get('/api/transactions?type=unpaid');

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.saveTransaction = function(transaction) {
    var promise = $http.post('/api/transactions', transaction);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };
}]);

swkroaApp.controller('dashboardController', function($scope, $http) {
  $http.get('/api/dashboard').success(function(data) {
    $scope.dashboard = data;
  });
});
