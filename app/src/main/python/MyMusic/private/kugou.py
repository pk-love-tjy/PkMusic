#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
@author: HJK
@file: kugou.py
@time: 2019-05-08
"""

import copy
import hashlib
import json
import queue

import re

import requests

from MyMusic import config
from MyMusic.api import MusicApi
from MyMusic.song import BasicSong


class KugouApi(MusicApi):
    session = copy.deepcopy(MusicApi.session)
    session.headers.update(
        {"referer": "http://m.kugou.com", "User-Agent": config.get("ios_headers")}
    )


class KugouSong(BasicSong):
    def __init__(self):
        super(KugouSong, self).__init__()
        self.hash = ""

    def download_lyrics(self):
        pass

    def download(self):
        pass

def getMp3Url(id) ->str:
    params = dict(cmd="playInfo", hash=id)
    res_data = KugouApi.request(
        "http://m.kugou.com/app/i/getSongInfo.php", method="GET", data=params
    )
    if not res_data.get("url", ""):
        return ""
    return res_data.get("url", "")



def kugou_search(keyword,allSong=None) -> list:
    """ 搜索音乐 """
    number = config.get("number") or 5
    params = dict(
        keyword=keyword, platform="WebFilter", format="json", page=1, pagesize=number
    )

    songs_list = []
    res_data = (
        KugouApi.request(
            "http://songsearch.kugou.com/song_search_v2", method="GET", data=params
        )
        .get("data", {})
        .get("lists", [])
    )

    for item in res_data:
        " vip   trans_param 下 musicpack_advance==1 "
        if item.get("trans_param",[]).get("musicpack_advance","")==1:
            continue
        song = KugouSong()
        song.source = "kugou"
        song.id = item.get("FileHash", "")
        song.title = item.get("SongName", "")
        song.singer = item.get("SingerName", "")
        song.duration = item.get("Duration", 0)
        song.album = item.get("AlbumName", "")
        song.size = round(item.get("FileSize", 0) / 1048576, 2)
        song.hash = item.get("FileHash", "")
        # 如果有更高品质的音乐选择高品质（尽管好像没什么卵用）
        keys_list = ["SQFileHash", "HQFileHash"]
        for key in keys_list:
            hash = item.get(key, "")
            if hash and hash != "00000000000000000000000000000000":
                song.id = hash
                break
        songs_list.append(song)
        if isinstance(allSong, queue.Queue):
            allSong.put(song.getObj())
    return songs_list



def kugou_playlist(open_url):
    # num_html=re.compile("(\d+)")
    # num=re.findall(num_html,open_url)[0]
    num="2nxXT0awqV2"
    url="https://www.kugou.com/yy/special/single/"+num+".html"
    result=requests.get(url).text
    print(result)
    find_data=re.compile("var data=(.*}?])")
    result=re.findall(find_data,result)[0]
    result=json.loads(result)
    gequdict = {}
    for data_song in result:
        print(data_song)
        song_hash=data_song["HASH"]
        songname=data_song["songname"]
        singername=data_song["singername"]
        m = hashlib.md5()
        m.update((song_hash + "kgcloudv2").encode("utf-8"))
        md = m.hexdigest()
        url="http://trackercdn.kugou.com/i/v2/?key=" + md + "&hash=" + song_hash + "&br=hq&appid=1005&pid=2&cmd=25&behavior=play"
        gequdict[songname+"*"+singername+"*kugou"]=url
    return gequdict


search = kugou_search
playlist = kugou_playlist
if __name__ == '__main__':
    search("海阔天空")






