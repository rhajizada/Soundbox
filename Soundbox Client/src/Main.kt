import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import kotlin.browser.*

external class XMLHttpRequest
var xhttp :dynamic= XMLHttpRequest(); // Adding native JavaScript library for http calls
val main_card = document.getElementById("main_card") as HTMLDivElement
val spotify_input = document.getElementById("spotify_input") as HTMLInputElement
val apple_input = document.getElementById("apple_input") as HTMLInputElement
val tidal_input = document.getElementById("tidal_input") as HTMLInputElement
val APILink: String = "http://localhost:8080"
var spotify_client_id = "c07ad48ee1484f41b704019a6fa07ca2"
var spotify_client_secret = "521d1a1f49f9497ab6c202f9851b8aef"
var spotifyAuthLink = "https://accounts.spotify.com/authorize?client_id=${spotify_client_id}&response_type=code&redirect_uri=http://localhost:8080&scope=user-read-private%20user-read-email&s"

fun main(args: Array<String>) {

    //tidal_input!!.placeholder = "I changed this using kotlin"
    val submitBtn = document.createElement("button") as HTMLButtonElement
    submitBtn.className = "btn btn-primary"
    submitBtn.type = "submit"
    submitBtn.innerText = "Submit"

    val authBtn = document.createElement("button") as HTMLButtonElement
    authBtn.className = "btn btn-primary"
    authBtn.type = "submit"
    authBtn.innerText = "Spotify Authentificate"

    main_card.appendChild(submitBtn)
    submitBtn.addEventListener("click", { printInputs(); sendAll()})
}

fun printInputs(){
    println("Spotify: ${spotify_input.value}")
    println("Apple Music: ${apple_input.value}")
    println("Tidal: ${tidal_input.value}")
}

// Example of async request
private fun getAsync(url: String, callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET",url) // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.setRequestHeader("link", spotify_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
        }
        xmlHttp.send()
    }
}

private fun spotifyAuth(){
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET",spotifyAuthLink) // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.setRequestHeader("link", spotify_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
               print("went right");
            }
        }
        xmlHttp.send()
    }
}

private fun SpotifyLink(callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET", APILink+"/spotify") // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.setRequestHeader("spotify-link", spotify_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
        }
        xmlHttp.send()
    }
}

private fun AppleLink(callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET", APILink+"/apple") // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.setRequestHeader("apple-link", apple_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
        }
        xmlHttp.send()
    }
}

private fun TidalLink(callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET", APILink+"/tidal") // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.setRequestHeader("tidal-link", tidal_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
        }
        xmlHttp.send()
    }
}

private fun sendAll(){
    if(!tidal_input.value.isNullOrEmpty()){
        TidalLink { response -> print(response)}
    }
    if(!spotify_input.value.isNullOrEmpty()){
        SpotifyLink { response -> print(response)}
    }
    if(!apple_input.value.isNullOrEmpty()){
        AppleLink  { response -> print(response)}
    }
}


