package com.soundboxserver
class Song(){
    lateinit var song: String
    lateinit var artist: String
    lateinit var album: String
    constructor(song: String, artist: String, album: String) : this() {
        this.song = song
        this.artist = artist
        this.album = album
    }
    constructor(platform: String, html: String) : this(){
        var htmlList = html.split('\n')
        when(platform){
            "spotify" -> {
                this.song = (htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[0].getRidOfWrong()) // Adding song name
                this.artist = (htmlList[2].split("<title>", "</title>")[1].split(", a song by ")[1].split(" on Spotify")[0].getRidOfWrong()) // Adding Artist name
                this.album = (htmlList[40].split("</a></div></section></div>")[0].split(">")[1].getRidOfWrong()) // Adding album name
            }
            "apple" -> {
                this.song = (htmlList[18].split("content=")[1].split(" by")[0].removeRange(0,1).getRidOfWrong()) // Song name works
                this.artist = (htmlList[18].split("by ")[1].split('"')[0].getRidOfWrong()) // Artist name
                this.album = (htmlList[14].split("listen, ")[1].split(", ${this.artist}")[0].getRidOfWrong()) // Album name testing
            }
            "tidal" -> {
                this.song = (htmlList[0].split("name")[18].removeRange(0..2).split("description")[0].reversed().removeRange(0..2).reversed().getRidOfWrong()) //Song name
                this.artist = (htmlList[0].split("name")[20].split("artist-list-link hover-desktop")[1].split("</a>")[0].removeRange(0..1).getRidOfWrong()) //Artist name
                this.album = (htmlList[0].split("name")[20].split("calc(33.33vw - 1.5rem), calc(100vw - 3rem)")[1].split(" class=")[0].removeRange(0..6).reversed().removeRange(0..0).reversed().getRidOfWrong()) //Album name
            }
            "deezer" -> {
                this.song = (htmlList[25].split("content=\"")[1].split(" - ")[0]) // Song name
                this.artist = (htmlList[25].split("content=\"")[1].split(" - ")[1])// Artist name
                this.album = (htmlList[26].split(" - ")[1].split(". Deezer")[0]) // Album
            }
            else -> {
                println("Wrong platform; Constructing empty song")
            }
        }
    }
    fun print(){
        println("Song: ${this.song} Artist: ${this.artist} Album: ${this.album}")
    }
}

class Response(val kind: String,val url: URL,val queries: Queries,val items: List<Item>) {
    fun printItems() {
        for (i in items) {
            i.print()
        }
    }
    fun getSpotifyLink(song: Song): String {
        if (items.isNullOrEmpty()) {
            return ""
        } else {
            var searchList: MutableList<Item> = mutableListOf<Item>()
            for (i in items.asReversed()) {
                if (i.link.contains("open.spotify.com/track")) {
                    searchList.add(i)
                }
            }
            if (searchList.isEmpty()) {
                for (i in items.asReversed()) {
                    if (i.link.contains("open.spotify.com/album")) {
                        searchList.add(i)
                    }
                }
            }
            if (searchList.isEmpty()) {
                for (i in items.asReversed()) {
                    if (i.link.contains("open.spotify.com/user")) {
                        searchList.add(i)
                    }
                }
            }
            if (searchList.isEmpty()) {
                return " "
            }
            else {
                return searchList[0].link
            }
        }
    }
    fun getAppleLink(song: Song): String{
        if(items.isNullOrEmpty()) {
            return ""
        } else {
            var searchList: MutableList<Item> = mutableListOf<Item>()
            for (i in items)
            {
                if (i.link.contains("https://itunes.apple.com/") && (i.link.contains("track")))
                {
                    searchList.add(i)
                }
            }
            if (searchList.isEmpty())
            {
                for (i in items)
                {
                    if (i.link.contains("https://itunes.apple.com/") && (i.link.contains("album")))
                    {
                        searchList.add(i)
                    }
                }
            }
            if (searchList.isEmpty())
            {
                return ""
            }
            else
            {
                return searchList[0].link
            }
        }
    }
    fun getTidalLink(song: Song): String{
        if(items.isNullOrEmpty())
        {
            return ""
        }
        else
        {
            var searchList: MutableList<Item> = mutableListOf<Item>()
            for (i in items)
            {
                if (i.link.contains("tidal.com/browse/track") /*|| i.link.contains("tidal.com/browse/track/")*/)
                {
                    searchList.add(i)
                }
            }
            if (searchList.isEmpty())
            {
                for (i in items)
                {
                    if (i.link.contains("tidal.com/browse/album") || i.link.contains("tidal.com/album") /*|| i.link.contains("tidal.com/browse/track/")*/)
                    {
                        searchList.add(i)
                    }
                }
            }
            if (searchList.isEmpty())
            {
                return ""
            }
            else
            {
                return searchList[0].link
            }
        }
    }
    fun getDeezerLink(song: Song): String {
        if (items.isNullOrEmpty())
        {
            return ""
        }
        else
        {
            var searchList: MutableList<Item> = mutableListOf<Item>()
            for (i in items)
            {
                if (i.link.contains("www.deezer.com") && i.link.contains("track"))
                {
                    searchList.add(i)
                }
            }
            if (searchList.isEmpty())
            {
                for (i in items)
                {
                    if (i.link.contains("www.deezer.com") && i.link.contains("album"))
                    {
                        searchList.add(i)
                    }
                }
            }
            if (searchList.isEmpty())
            {
                return ""
            }
            else
            {
                return searchList[0].link
            }
        }
    }
}


class URL(val type: String,val template: String)
class Queries(val requests: List<Request>,val nextPages: List<NextPage>)
class Request(val title: String,val totalResults: String,val searchTerms: String,val count: Int,val startIndex: Int,val inputEncoding: String,val outputEncoding: String,val safe: String,val cx: String)
class NextPage(val title: String,val totalResults: String,val searchTerms: String,val count: Int,val startIndex: Int,val inputEncoding: String,val outputEncoding: String,val safe: String,val cx: String)
class Item(val kind: String,val title: String,val htmlTitle: String,val link: String,val displayLink: String,val snippet: String,val htmlSnippet: String,val cache_id: String,val formattedUrl: String,val htmlFormattedUrl: String){
    fun print(){
        println("Kind: $kind\nTitle: $title\nhtmlTitle: ${htmlTitle}\nlink: $link\ndisplayLink: $displayLink\nsnippet: $snippet\nhtmlSnippet: ${htmlSnippet}\ncache_id: $cache_id\nformattedUrl: $formattedUrl\nhtmlFormattedUrl: $htmlFormattedUrl")
    }
}