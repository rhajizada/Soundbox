import org.w3c.dom.*
import kotlin.browser.*
import kotlin.dom.addClass

external class XMLHttpRequest
var xhttp :dynamic= XMLHttpRequest(); // Adding native JavaScript library for http calls
//val main_card = document.getElementById("main_card") as HTMLDivElement
//val spotify_input = document.getElementById("spotify_input") as HTMLInputElement
//val apple_input = document.getElementById("apple_input") as HTMLInputElement
//val tidal_input = document.getElementById("tidal_input") as HTMLInputElement
val APILink: String = "http://localhost:8080"

var search_input = document.getElementById("search") as HTMLInputElement
val search_button = document.getElementById("search_button") as HTMLButtonElement

val songCard = document.getElementById("song_card") as HTMLDivElement
val artworkImage = document.getElementById("artwork") as HTMLImageElement
val songInfo = document.getElementById("song_info") as HTMLHeadingElement
val spotify_link = document.getElementById("spotify_link") as HTMLAnchorElement
val apple_link = document.getElementById("apple_link") as HTMLAnchorElement
val tidal_link = document.getElementById("tidal_link") as HTMLAnchorElement

//val submitBtn = document.createElement("button") as HTMLButtonElement

//    <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>


val authBtn = document.createElement("button") as HTMLButtonElement

fun main(args: Array<String>) {
    songCard.hidden = true
    //tidal_input!!.placeholder = "I changed this using kotlin"
    authBtn.className = "btn btn-primary"
    authBtn.type = "submit"
    authBtn.innerText = "Spotify Authentificate"
    search_button.addEventListener("click", {
        println(search_input.value)
        if(search_input.value.isNullOrEmpty()){
            window.alert("Search bar is empty")
        }
        else{
            if(search_input.value.contains("open.spotify")){
            SpotifyLink {response -> parseResponse(response)}
            }
            if(search_input.value.contains("itunes")){
            AppleLink {response -> parseResponse(response)}
            }
            if(search_input.value.contains("tidal")){
            TidalLink {response -> parseResponse(response)}
            }
            if(search_input.value.contains("deezer")){
                DeezerLink {response -> parseResponse(response)}
            }
    }
    })
}





// Example of async request



private fun SpotifyLink(callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET", APILink+"/spotify") // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.setRequestHeader("spotify-link", search_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
            else{
                window.alert("Song could not be parsed")
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
        xmlHttp.setRequestHeader("apple-link", search_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
            else{
                window.alert("Song could not be parsed")
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
        xmlHttp.setRequestHeader("tidal-link", search_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
            else{
                window.alert("Song could not be parsed")
            }
        }
        xmlHttp.send()
    }
}

private fun DeezerLink(callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET", APILink+"/deezer") // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        println("trying to get deezer")
        xmlHttp.setRequestHeader("deezer-link", search_input.value)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
            else{
                window.alert("Song could not be parsed")
            }
        }
        xmlHttp.send()
    }
}


//private fun sendAll(){
//    if(!tidal_input.value.isNullOrEmpty()){
//        if(!(tidal_input.value.contains("https://"))){
//            tidal_input.value = "https://" + tidal_input.value
//        }
//        TidalLink { response -> parseResponse(response)}
//    }
//    if(!spotify_input.value.isNullOrEmpty()){
//        if(!(spotify_input.value.contains("https://"))){
//            spotify_input.value = "https://" + spotify_input.value
//        }
//        SpotifyLink { response -> parseResponse(response)}
//    }
//    if(!apple_input.value.isNullOrEmpty()){
//        if(!(apple_input.value.contains("https://"))){
//            apple_input.value = "https://" + apple_input.value
//        }
//        AppleLink  { response -> parseResponse(response)}
//    }
//}

private fun parseResponse(x: String){
    var links = x.split("\n")
    var song = links[0].split("Song: ")[1]
    var artist = links[1].split("Artist: ")[1]
    var album = links[2].split("Album: ")[1]
    var artwork = links[3].split("Artwork: ")[1]
    var spotify = links[4].split("Spotify: ")[1]
    var apple = links[5].split("Apple: ")[1]
    var tidal = links[6].split("Tidal: ")[1]
    songCard.hidden = false
    artworkImage.src = artwork
    songInfo.innerHTML = "$song </br> $artist </br> $album"
    if(spotify.contains("spotify")){
        spotify_link.innerText = "Spotify"
        spotify_link.href = spotify
    }
    else{
        spotify_link.innerText = "Song could not be found on Spotify"
    }
    if(tidal.contains("tidal")){
        tidal_link.innerText = "Tidal"
        tidal_link.href = tidal
    }
    else{
        tidal_link.innerText = "Song could not be found on Tidal"
    }
    if(apple.contains("itunes")){
        apple_link.innerText = "Apple Music"
        apple_link.href = apple
    }
    else{
        apple_link.innerText = "Song could not be found on Apple Music"
    }



    println("Song: $song\nArtist: $artist\nAlbum: $album\nArtwork: $artwork\nSpotify: $spotify\nApple: $apple\nTidal: $tidal")
}


