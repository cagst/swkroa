/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var swkroaApp = angular.module('swkroaApp', ['ui.router']);

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
    var promise = $http.get('../svc/codeset/ADDRESS_TYPE').then(function(response) {
      return response.data;
    });

    return promise;
  };

  this.getPhoneTypes = function() {
    var promise = $http.get('../svc/codeset/PHONE_TYPE').then(function(response) {
      return reponse.data;
    });

    return promise;
  };

  this.getEmailTypes = function() {
    var promise = $http.get('../svc/codeset/EMAIL_TYPE').then(function(response) {
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
    if (entity.addressUID > 0) {
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

  this.syncAddressTypes = function(addresses, types) {
    for (var idx1 = 0; idx1 < addresses.length; idx1++) {
      for (var idx2 = 0; idx2 < types.length; idx2++) {
        if (addresses[idx1].addressType.codeValueUID == types[idx2].codeValueUID) {
          addresses[idx1].addressType = types[idx2];
          break;
        }
      }
    }
  };

  this.syncPhoneTypes = function(phones, types) {
    for (var idx1 = 0; idx1 < phones.length; idx1++) {
      for (var idx2 = 0; idx2 < types.length; idx2++) {
        if (phones[idx1].phoneType.codeValueUID == types[idx2].codeValueUID) {
          phones[idx1].phoneType = types[idx2];
          break;
        }
      }
    }
  };

  this.syncEmailTypes = function(emails, types) {
    for (var idx1 = 0; idx1 < emails.length; idx1++) {
      for (var idx2 = 0; idx2 < types.length; idx2++) {
        if (emails[idx1].emailType.codeValueUID == types[idx2].codeValueUID) {
          emails[idx1].emailType = types[idx2];
          break;
        }
      }
    }
  };
}]);
