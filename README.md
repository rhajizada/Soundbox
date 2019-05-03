# Soundbox
Soundbox is a service that allows users of different music streaming services sharing songs with each other.
# Supported Services
- Spotify
- Apple Music
- Tidal
- Deezer
# App Components
- Soundbox Client ( KotlinJS Website )
- Soundbox Server ( KotlinJVM + KTor )
# Do before installation
Please add the Google CSE API Key and CX in order install and run server part properly
# How to install and run
**Installing and running Server**
```
cd $(Soundbox Server)
./gradlew. run
```
**Running client***
Since client is just a website with KotlinJS part already compile into JavaScript file, you can either open the index.html in your browse or if you python pre-installed run:
```
cd $(Soundbox Client)
python -m SimpleHTTPServer 8000
```
And navigate to localhost:8000 on your browser

# Using server for your own client
If you want to write your own client for the server you can do it since server is essentially a RESTFul API.
***How to use***
```
curl --header "platform: $platform" --header "link: $link" http://localhost:8080/song-info
```
Response you will get will be formatted in this way:

```
{
  Song: $song-name
  Artist: $artist
  Artwork: $album-artwork-link
  Spotify: $spotify-link
  Apple: $apple-music-link
  Tidal: $tidal-link
  Deezer: $deezer-link
}
```
***Response Codes***

200 - OK
401 - Problem with the link
402 - Link formatted wrong ( link does not have "http://www." in the beginning)
