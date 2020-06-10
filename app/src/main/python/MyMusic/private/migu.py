#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
@author: HJK
@file: migu
@time: 2019-08-25
"""

import copy
import queue

import requests

from MyMusic import config
from MyMusic.api import MusicApi
from MyMusic.song import BasicSong


class MiguApi(MusicApi):
    session = copy.deepcopy(MusicApi.session)
    session.headers.update(
        {"referer": "http://music.migu.cn/", "User-Agent": config.get("ios_useragent")}
    )


class MiguSong(BasicSong):
    def __init__(self):
        super(MiguSong, self).__init__()


def migu_search(keyword,allSong=None) -> list:
    """ 搜索音乐 """
    number = config.get("number") or 5
    params = {
        "keyword":keyword,
        "type":2,
        "rows":number,
        "pgc":1
    }

    songs_list = []
    MiguApi.session.headers.update(
        {"referer": "http://music.migu.cn/", "User-Agent": config.get("ios_useragent")}
    )
    res_data = (
        MiguApi.request(
            "https://m.music.migu.cn/migu/remoting/scr_search_tag",
            method="GET",
            data=params,
        )
        .get("musics", [])
    )

    for item in res_data:
        song = MiguSong()
        song.source = "migu"
        song.id = item.get("mp3", "")
        song.title = item.get("title", "")
        song.singer = item.get("singerName", "")
        song.album = item.get("albumName", "")
        song.cover_url = item.get("cover", "")
        song.lyrics_url = item.get("lyrics", "")
        song._song_url = item.get("mp3","")
        songs_list.append(song)
        if isinstance(allSong, queue.Queue):
            allSong.put(song.getObj())
    return songs_list

def getMp3Url(key) -> str:
    return key

search = migu_search
if __name__ == '__main__':
    for i in search("广寒宫"):
        print(getMp3Url(i.getObj()["id"]))
        pass