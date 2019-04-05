if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'Soundbox'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'Soundbox'.");
}
var Soundbox = function (_, Kotlin) {
  'use strict';
  var throwCCE = Kotlin.throwCCE;
  var Unit = Kotlin.kotlin.Unit;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var toShort = Kotlin.toShort;
  var print = Kotlin.kotlin.io.print_s8jyv4$;
  var xhttp;
  var main_card;
  var spotify_input;
  var apple_input;
  var tidal_input;
  var APILink;
  var spotify_client_id;
  var spotify_client_secret;
  var spotifyAuthLink;
  function main$lambda(it) {
    printInputs();
    sendAll();
    return Unit;
  }
  function main(args) {
    var tmp$, tmp$_0;
    var submitBtn = Kotlin.isType(tmp$ = document.createElement('button'), HTMLButtonElement) ? tmp$ : throwCCE();
    submitBtn.className = 'btn btn-primary';
    submitBtn.type = 'submit';
    submitBtn.innerText = 'Submit';
    var authBtn = Kotlin.isType(tmp$_0 = document.createElement('button'), HTMLButtonElement) ? tmp$_0 : throwCCE();
    authBtn.className = 'btn btn-primary';
    authBtn.type = 'submit';
    authBtn.innerText = 'Spotify Authentificate';
    main_card.appendChild(submitBtn);
    submitBtn.addEventListener('click', main$lambda);
  }
  function printInputs() {
    println('Spotify: ' + spotify_input.value);
    println('Apple Music: ' + apple_input.value);
    println('Tidal: ' + tidal_input.value);
  }
  function getAsync$lambda(closure$xmlHttp, closure$callback) {
    return function () {
      if (closure$xmlHttp.readyState == toShort(4) && closure$xmlHttp.status == toShort(200)) {
        closure$callback(closure$xmlHttp.responseText);
      }
      return Unit;
    };
  }
  function getAsync(url, callback) {
    var xmlHttp = new XMLHttpRequest();
    if (xmlHttp) {
      xmlHttp.open('GET', url);
      xmlHttp.withCredentials = true;
      xmlHttp.setRequestHeader('link', spotify_input.value);
      xmlHttp.onload = getAsync$lambda(xmlHttp, callback);
      xmlHttp.send();
    }
  }
  function spotifyAuth$lambda(closure$xmlHttp) {
    return function () {
      if (closure$xmlHttp.readyState == toShort(4) && closure$xmlHttp.status == toShort(200)) {
        print('went right');
      }
      return Unit;
    };
  }
  function spotifyAuth() {
    var xmlHttp = new XMLHttpRequest();
    if (xmlHttp) {
      xmlHttp.open('GET', spotifyAuthLink);
      xmlHttp.withCredentials = true;
      xmlHttp.setRequestHeader('link', spotify_input.value);
      xmlHttp.onload = spotifyAuth$lambda(xmlHttp);
      xmlHttp.send();
    }
  }
  function SpotifyLink$lambda(closure$xmlHttp, closure$callback) {
    return function () {
      if (closure$xmlHttp.readyState == toShort(4) && closure$xmlHttp.status == toShort(200)) {
        closure$callback(closure$xmlHttp.responseText);
      }
      return Unit;
    };
  }
  function SpotifyLink(callback) {
    var xmlHttp = new XMLHttpRequest();
    if (xmlHttp) {
      xmlHttp.open('GET', APILink + '/spotify');
      xmlHttp.withCredentials = true;
      xmlHttp.setRequestHeader('spotify-link', spotify_input.value);
      xmlHttp.onload = SpotifyLink$lambda(xmlHttp, callback);
      xmlHttp.send();
    }
  }
  function AppleLink$lambda(closure$xmlHttp, closure$callback) {
    return function () {
      if (closure$xmlHttp.readyState == toShort(4) && closure$xmlHttp.status == toShort(200)) {
        closure$callback(closure$xmlHttp.responseText);
      }
      return Unit;
    };
  }
  function AppleLink(callback) {
    var xmlHttp = new XMLHttpRequest();
    if (xmlHttp) {
      xmlHttp.open('GET', APILink + '/apple');
      xmlHttp.withCredentials = true;
      xmlHttp.setRequestHeader('apple-link', apple_input.value);
      xmlHttp.onload = AppleLink$lambda(xmlHttp, callback);
      xmlHttp.send();
    }
  }
  function TidalLink$lambda(closure$xmlHttp, closure$callback) {
    return function () {
      if (closure$xmlHttp.readyState == toShort(4) && closure$xmlHttp.status == toShort(200)) {
        closure$callback(closure$xmlHttp.responseText);
      }
      return Unit;
    };
  }
  function TidalLink(callback) {
    var xmlHttp = new XMLHttpRequest();
    if (xmlHttp) {
      xmlHttp.open('GET', APILink + '/tidal');
      xmlHttp.withCredentials = true;
      xmlHttp.setRequestHeader('tidal-link', tidal_input.value);
      xmlHttp.onload = TidalLink$lambda(xmlHttp, callback);
      xmlHttp.send();
    }
  }
  function sendAll() {
    println('Tidal: ' + tidal_input.value);
    println('Spotify: ' + spotify_input.value);
    println('Apple: ' + apple_input.value);
  }
  Object.defineProperty(_, 'xhttp', {
    get: function () {
      return xhttp;
    },
    set: function (value) {
      xhttp = value;
    }
  });
  Object.defineProperty(_, 'main_card', {
    get: function () {
      return main_card;
    }
  });
  Object.defineProperty(_, 'spotify_input', {
    get: function () {
      return spotify_input;
    }
  });
  Object.defineProperty(_, 'apple_input', {
    get: function () {
      return apple_input;
    }
  });
  Object.defineProperty(_, 'tidal_input', {
    get: function () {
      return tidal_input;
    }
  });
  Object.defineProperty(_, 'APILink', {
    get: function () {
      return APILink;
    }
  });
  Object.defineProperty(_, 'spotify_client_id', {
    get: function () {
      return spotify_client_id;
    },
    set: function (value) {
      spotify_client_id = value;
    }
  });
  Object.defineProperty(_, 'spotify_client_secret', {
    get: function () {
      return spotify_client_secret;
    },
    set: function (value) {
      spotify_client_secret = value;
    }
  });
  Object.defineProperty(_, 'spotifyAuthLink', {
    get: function () {
      return spotifyAuthLink;
    },
    set: function (value) {
      spotifyAuthLink = value;
    }
  });
  _.main_kand9s$ = main;
  _.printInputs = printInputs;
  xhttp = new XMLHttpRequest();
  var tmp$, tmp$_0, tmp$_1, tmp$_2;
  main_card = Kotlin.isType(tmp$ = document.getElementById('main_card'), HTMLDivElement) ? tmp$ : throwCCE();
  spotify_input = Kotlin.isType(tmp$_0 = document.getElementById('spotify_input'), HTMLInputElement) ? tmp$_0 : throwCCE();
  apple_input = Kotlin.isType(tmp$_1 = document.getElementById('apple_input'), HTMLInputElement) ? tmp$_1 : throwCCE();
  tidal_input = Kotlin.isType(tmp$_2 = document.getElementById('tidal_input'), HTMLInputElement) ? tmp$_2 : throwCCE();
  APILink = 'http://localhost:8080';
  spotify_client_id = 'c07ad48ee1484f41b704019a6fa07ca2';
  spotify_client_secret = '521d1a1f49f9497ab6c202f9851b8aef';
  spotifyAuthLink = 'https://accounts.spotify.com/authorize?client_id=' + spotify_client_id + '&response_type=code&redirect_uri=http://localhost:8080&scope=user-read-private%20user-read-email&s';
  main([]);
  Kotlin.defineModule('Soundbox', _);
  return _;
}(typeof Soundbox === 'undefined' ? {} : Soundbox, kotlin);
