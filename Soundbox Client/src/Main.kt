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
        TidalLink { response -> parseResponse(response)}
    }
    if(!spotify_input.value.isNullOrEmpty()){
        SpotifyLink { response -> parseResponse(response)}
    }
    if(!apple_input.value.isNullOrEmpty()){
        AppleLink  { response -> parseResponse(response)}
    }
}

private fun parseResponse(x: String){
    println(x)
    var links = x.split(",")
    spotify_input.value = links[0].split("{Spotify=")[1]
    apple_input.value = links[1].split("Apple=")[1]
    tidal_input.value = links[2].split("Tidal=")[1].reversed().removeRange(0..1).reversed()
}


