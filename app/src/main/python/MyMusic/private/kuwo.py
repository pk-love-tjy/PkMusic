
import copy
import json
import queue
import requests
from MyMusic import config
from MyMusic.api import MusicApi
from MyMusic.song import BasicSong
from urllib.parse import quote

class KuwoApi(MusicApi):
    session = copy.deepcopy(MusicApi.session)



class KuwoSong(BasicSong):
    def __init__(self):
        super(KuwoSong, self).__init__()


    def download_lyrics(self):
        pass

    def download(self):
        pass

def getMp3Url(id) ->str:
    params = dict(type="convert_url",format="mp3",response="url",rid=id)
    return requests.get("http://antiserver.kuwo.cn/anti.s",headers=config.get("fake_headers"),params=params).text



from random import randint
def kuwo_search(keyword,allSong=None) -> list:
    """ 搜索音乐 """
    keyword = quote(keyword)
    number = config.get("number") or 5
    token=randint(1000000000,9999999999)
    KuwoApi.session.headers.update(
        {   "Referer": "http://kuwo.cn/search/list?key=" + keyword,
            "csrf":str(token),
            "Cookie":"kw_token=" +str(token),
         }
    )
    songs_list = []
    res_data = (
        KuwoApi.request(
            "http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key=" + keyword + "&pn=" + str(1) + "&rn="+str(number), method="GET"
        )
        .get("data", {})
        .get("list", [])
    )
    for item in res_data:
        if "%" in item.get("name", ""):
            continue
        song = KuwoSong()
        song.source = "kuwo"
        song.id = item.get("musicrid", "")
        song.title = item.get("name", "")
        song.singer = item.get("artist", "")
        song.album = item.get("album", "")
        songs_list.append(song)
        if isinstance(allSong, queue.Queue):
            allSong.put(song.getObj())
    return songs_list


def kuwo_playlist(url):
    pass


search = kuwo_search
playlist = kuwo_playlist


if __name__ == '__main__':
    search("广寒宫丸子")