if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'Soundbox'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'Soundbox'.");
}
var Soundbox = function (_, Kotlin) {
  'use strict';
  var throwCCE = Kotlin.throwCCE;
  var Unit = Kotlin.kotlin.Unit;
  var print = Kotlin.kotlin.io.print_s8jyv4$;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var toShort = Kotlin.toShort;
  var xhttp;
  var main_card;
  var spotify_input;
  var apple_input;
  var tidal_input;
  function main$lambda(it) {
    printInputs();
    return Unit;
  }
  function main$lambda$lambda(response) {
    print(response);
    return Unit;
  }
  function main$lambda_0(it) {
    getAsync('http://0.0.0.0:8080/html-dsl', main$lambda$lambda);
    return Unit;
  }
  function main(args) {
    var tmp$, tmp$_0;
    var submitBtn = Kotlin.isType(tmp$ = document.createElement('button'), HTMLButtonElement) ? tmp$ : throwCCE();
    submitBtn.className = 'btn btn-primary';
    submitBtn.type = 'submit';
    submitBtn.innerText = 'Submit';
    var requestBtn = Kotlin.isType(tmp$_0 = document.createElement('button'), HTMLButtonElement) ? tmp$_0 : throwCCE();
    requestBtn.className = 'btn btn-primary';
    requestBtn.type = 'submit';
    requestBtn.innerText = 'Request sample';
    main_card.appendChild(submitBtn);
    main_card.appendChild(requestBtn);
    submitBtn.addEventListener('click', main$lambda);
    requestBtn.addEventListener('click', main$lambda_0);
  }
  function printInputs() {
    println('Spotify: ' + spotify_input.value);
    println('Apple Music: ' + apple_input.value);
    println('Tidal: ' + tidal_input.value);
  }
  function getRequest$lambda(closure$response) {
    return function () {
      var tmp$;
      println(xhttp.readyState);
      println(xhttp.status);
      closure$response.v = typeof (tmp$ = xhttp.responseText) === 'string' ? tmp$ : throwCCE();
    };
  }
  function getRequest(url) {
    var response = {v: ' '};
    xhttp.open('GET', url, true);
    xhttp.onreadystatechange = getRequest$lambda(response);
    xhttp.send();
    return response.v;
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
      xmlHttp.onload = getAsync$lambda(xmlHttp, callback);
      xmlHttp.send();
    }
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
  _.main_kand9s$ = main;
  _.printInputs = printInputs;
  _.getRequest_61zpoe$ = getRequest;
  xhttp = new XMLHttpRequest();
  var tmp$, tmp$_0, tmp$_1, tmp$_2;
  main_card = Kotlin.isType(tmp$ = document.getElementById('main_card'), HTMLDivElement) ? tmp$ : throwCCE();
  spotify_input = Kotlin.isType(tmp$_0 = document.getElementById('spotify_input'), HTMLInputElement) ? tmp$_0 : throwCCE();
  apple_input = Kotlin.isType(tmp$_1 = document.getElementById('apple_input'), HTMLInputElement) ? tmp$_1 : throwCCE();
  tidal_input = Kotlin.isType(tmp$_2 = document.getElementById('tidal_input'), HTMLInputElement) ? tmp$_2 : throwCCE();
  main([]);
  Kotlin.defineModule('Soundbox', _);
  return _;
}(typeof Soundbox === 'undefined' ? {} : Soundbox, kotlin);
