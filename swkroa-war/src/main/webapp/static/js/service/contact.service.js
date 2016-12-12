/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Defines the service for Memberships.
 *
 * Author: Craig Gaskill
 */

(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').service('ContactService', ContactService);

  function ContactService() {
    this.getStates = function() {
      return [
        {code:"AL", name:"Alabama"},
        {code:"AK", name:"Alaska"},
        {code:"AS", name:"American Samoa"},
        {code:"AZ", name:"Arizona"},
        {code:"AR", name:"Arkansas"},
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
        entity.addresses = [];
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
        entity.phoneNumbers = [];
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
        entity.emailAddresses = [];
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
  }

})(window, window.angular);
