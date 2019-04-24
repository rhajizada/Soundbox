import org.w3c.dom.*
import kotlin.browser.*
import kotlin.dom.addClass

external class XMLHttpRequest
var xhttp :dynamic= XMLHttpRequest(); // Adding native JavaScript library for http calls
val APILink: String = "http://localhost:8080"
var search_input = document.getElementById("search") as HTMLInputElement
val search_button = document.getElementById("search_button") as HTMLButtonElement
val songCard = document.getElementById("song_card") as HTMLDivElement
val artworkImage = document.getElementById("artwork") as HTMLImageElement
val songInfo = document.getElementById("song_info") as HTMLHeadingElement
val spotify_link = document.getElementById("spotify_link") as HTMLAnchorElement
val apple_link = document.getElementById("apple_link") as HTMLAnchorElement
val tidal_link = document.getElementById("tidal_link") as HTMLAnchorElement
val deezer_link = document.getElementById("deezer_link") as HTMLAnchorElement

fun main(args: Array<String>) {
    songCard.hidden = true
    search_button.addEventListener("click", {
        println(search_input.value)
        if(search_input.value.isNullOrEmpty()){
            window.alert("Search bar is empty")
        }
        else{
            if(search_input.value.contains("open.spotify")){
                checkSong(search_input.value, "spotify"){response -> parseResponse(response)}

            }
            if(search_input.value.contains("itunes")){
                checkSong(search_input.value, "apple"){response -> parseResponse(response)}
            }
            if(search_input.value.contains("tidal")){
                checkSong(search_input.value, "tidal"){response -> parseResponse(response)}
            }
            if(search_input.value.contains("deezer")){
                checkSong(search_input.value, "deezer"){response -> parseResponse(response)}
            }
    }
    })
}

// Example of async request
private fun checkSong(link: String, platform: String, callback: (String) -> Unit) {
    val xmlHttp: dynamic = XMLHttpRequest()
    if(xmlHttp) {
        xmlHttp.open("GET", APILink+"/song") // Allows us easily bypass CORS
        xmlHttp.withCredentials = true
        xmlHttp.setRequestHeader("link", link)
        xmlHttp.setRequestHeader("platform", platform)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
            else{
                window.alert("Server did not respond")
            }
        }
        xmlHttp.send()
    }
}

private fun parseResponse(x: String){
    var links = x.split("\n")
    var song = links[0].split("Song: ")[1]
    var artist = links[1].split("Artist: ")[1]
    var album = links[2].split("Album: ")[1]
    var artwork = links[3].split("Artwork: ")[1]
    var spotify = links[4].split("Spotify: ")[1]
    var apple = links[5].split("Apple: ")[1]
    var tidal = links[6].split("Tidal: ")[1]
    var deezer = links[7].split("Deezer: ")[1]
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
    if(deezer.contains("deezer")){
        deezer_link.innerText = "Deezer"
        deezer_link.href = deezer
    }
    else{
        deezer_link.innerText = "Song could not be found on Deezer"
    }
    println("Song: $song\nArtist: $artist\nAlbum: $album\nArtwork: $artwork\nSpotify: $spotify\nApple: $apple\nTidal: $tidal\nDeezer: $deezer")
}


