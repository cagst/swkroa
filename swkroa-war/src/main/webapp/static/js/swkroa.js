/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var swkroaApp = angular.module('swkroaApp',
  ['ui.bootstrap',
   'ui.utils',
   'ui.router',
   'xeditable']);

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

// add an interceptor to our http service to inject the context root for our requests
swkroaApp.factory('contextRootInterceptor', function($location) {
  return {
    'request': function(config) {
      if (config.url.startsWith("/")) {
        config.url = "/swkroa-war" + config.url;
      }

      return config;
    }
  };
});

swkroaApp.config(['$httpProvider', function($httpProvider) {
  $httpProvider.interceptors.push('contextRootInterceptor');
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

  this.getAddressTypes = function() {
    var promise = $http.get('/api/codeset/ADDRESS_TYPE').then(function(response) {
      return response.data;
    });

    return promise;
  };

  this.getPhoneTypes = function() {
    var promise = $http.get('/api/codeset/PHONE_TYPE').then(function(response) {
      return response.data;
    });

    return promise;
  };

  this.getEmailTypes = function() {
    var promise = $http.get('/api/codeset/EMAIL_TYPE').then(function(response) {
      return response.data;
    });

    return promise;
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
