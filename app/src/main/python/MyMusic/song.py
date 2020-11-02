
import re
import json


class BasicSong:
    """
        Define the basic properties and methods of a song.
        Such as title, name, singer etc.
    """

    def __init__(self):
        self.id = 0
        self.title = ""
        self.singer = ""

        self.album = ""
        self.size = ""

        self.duration = ""

        self.hash = ""
        self.mid = ""

        self.source = ""
        self.song_url = ""
        # self.song_file = ""
        self.cover_url = ""
        # self.cover_file = ""
        self.lyrics_url = ""


    def __str__(self):
        """ Song details """
        return json.dumps({
            "id": self.id,
            "title": self.title,
            "singer": self.singer,
            "album": self.album,
            "duration": self.duration,
            "size": self.size,
            "source":self.source,
            "song_url": self.song_url,
            "lyrics_url": self.lyrics_url,
            "cover_url": self.cover_url,
            "hash":self.hash,
            "mid":self.mid
        })
    def getObj(self):
        return {
            "source": self.source,
            "id": self.id,
            "title": self.title,
            "singer": self.singer,
            "album": self.album,
            "duration": self.duration,
            "size": self.size,

            "song_url": self.song_url,
            "lyrics_url": self.lyrics_url,
            "cover_url": self.cover_url,
            "hash": self.hash,
            "mid": self.mid
        }
