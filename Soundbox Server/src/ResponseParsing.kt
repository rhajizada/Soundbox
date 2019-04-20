package com.soundboxserver
class Song(){
    lateinit var song: String
    lateinit var artist: String
    lateinit var album: String
    constructor(song: String,artist: String,album: String) : this() {
        this.song = song
        this.artist = artist
        this.song = song
    }
    constructor(platform: String, html: String) : this(){
        var htmlList = html.split('\n')
        if(platform.equals("spotify", false)){
            this.song = (htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[0].getRidOfWrong()) // Adding song name
            this.artist = (htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[1].split(" on Spotify")[0].getRidOfWrong()) // Adding Artist name
            this.album = (htmlList[40].split("</a></div></section></div>")[0].split(">")[1].getRidOfWrong()) // Adding album name
        }

        if(platform.equals("apple", false)){
            this.song = (htmlList[18].split("content=")[1].split(" by")[0].removeRange(0,1).getRidOfWrong()) // Song name works
            this.artist = (htmlList[18].split("by ")[1].split('"')[0].getRidOfWrong()) // Artist name
            this.album = (htmlList[14].split("listen, ")[1].split(", ${this.artist}")[0].getRidOfWrong()) // Album name testing
        }
        if(platform.equals("tidal", false)){
            this.song = (htmlList[0].split("name")[18].removeRange(0..2).split("description")[0].reversed().removeRange(0..2).reversed().getRidOfWrong()) //Song name
            this.artist = (htmlList[0].split("name")[20].split("artist-list-link hover-desktop")[1].split("</a>")[0].removeRange(0..1).getRidOfWrong()) //Artist name
            this.album = (htmlList[0].split("name")[20].split("calc(33.33vw - 1.5rem), calc(100vw - 3rem)")[1].split(" class=")[0].removeRange(0..6).reversed().removeRange(0..0).reversed().getRidOfWrong()) //Album name
        }

    }
    fun print(){
        println("Song: ${this.song} Artist: ${this.artist} Album: ${this.album}")
    }
}

class Response(val kind: String,val url: URL,val queries: Queries,val items: List<Item>){
    fun printItems(){
        for(i in items){
            i.print()
        }
    }
    fun getSpotifyLink(song: Song): String{
        var searchList: MutableList<Item> = mutableListOf<Item>()
        for(i in items.asReversed()){
            if(i.link.contains("open.spotify.com/album") || i.link.contains("open.spotify.com/track")){
                searchList.add(i)
            }
        }
//        for(i in searchList){
//            if (!(i.snippet.contains(song.album))){
//                searchList.remove(i)
//            }
//        }
//        println("Printing spotify links: ")
//        for(i in searchList){
//            i.print()
//            println("")
//        }
        if(searchList.isEmpty()){
            return ""
        }
        else {
            return searchList[0].link
        }
    }

    fun getAppleLink(song: Song): String{
        var searchList: MutableList<Item> = mutableListOf<Item>()
        for(i in items){
            if(i.link.contains("https://itunes.apple.com/us/album/") || i.link.contains("https://itunes.apple.com/us/track/")){
                searchList.add(i)
            }
        }
//        for(i in searchList){
//            if (!i.snippet.contains(song.album)){
//             searchList.remove(i)
//            }
//        }
//        println("Printing apple links: ")
//        for(i in searchList){
//            i.print()
//            println("")
//        }
        if(searchList.isEmpty()){
            return ""
        }
        else {
            return searchList[0].link
        }
    }
    fun getTidalLink(song: Song): String{
        var searchList: MutableList<Item> = mutableListOf<Item>()
        for(i in items){
            println(i.link)
            if(i.link.contains("tidal.com/browse/") /*|| i.link.contains("tidal.com/browse/track/")*/){
                searchList.add(i)
            }
        }
//        for(i in searchList){
//            if (!i.snippet.contains(song.album)){
//                searchList.remove(i)
//            }
//        }
//        println("Printing tidal links: ")
//        for(i in searchList){
//            i.print()
//            println("")
//        }
        if(searchList.isEmpty()){
            return ""
        }
        else {
            return items[0].link
        }
    }
}


class URL(val type: String,val template: String)
class Queries(val requests: List<Request>,val nextPages: List<NextPage>)
class Request(val title: String,val totalResults: String,val searchTerms: String,val count: Int,val startIndex: Int,val inputEncoding: String,val outputEncoding: String,val safe: String,val cx: String)
class NextPage(val title: String,val totalResults: String,val searchTerms: String,val count: Int,val startIndex: Int,val inputEncoding: String,val outputEncoding: String,val safe: String,val cx: String)
class Context(val title: String)
class SearchInformation(val searchTime: Double,val formattedSearchTime: String,val totalResults: String,val formattedTotalResults: String)
class Item(val kind: String,val title: String,val htmlTitle: String,val link: String,val displayLink: String,val snippet: String,val htmlSnippet: String,val cache_id: String,val formattedUrl: String,val htmlFormattedUrl: String){
    fun print(){
        println("Kind: $kind\nTitle: $title\nhtmlTitle: ${htmlTitle}\nlink: $link\ndisplayLink: $displayLink\nsnippet: $snippet\nhtmlSnippet: ${htmlSnippet}\ncache_id: $cache_id\nformattedUrl: $formattedUrl\nhtmlFormattedUrl: $htmlFormattedUrl")
    }
}