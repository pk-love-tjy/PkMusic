#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
@author: HJK
@file: qq.py
@time: 2019-05-08
"""
import json
import queue
import random
import copy

import re
import requests

from MyMusic import config
from MyMusic.api import MusicApi
from MyMusic.song import BasicSong


class QQApi(MusicApi):
    session = copy.deepcopy(MusicApi.session)
    session.headers.update(
        {
            "referer": "https://y.qq.com/portal/player.html",
            "User-Agent": config.get("ios_useragent"),
        }
    )


class QQSong(BasicSong):
    def __init__(self):
        super(QQSong, self).__init__()


    def download_lyrics(self):
        pass

    def download_cover(self):
        pass

    def download(self):
        pass


def qq_search(keyword,allSong=None) -> list:
    """ 搜索音乐 """
    number = config.get("number") or 5
    params = {"w": keyword, "format": "json", "p": 1, "n": number}

    songs_list = []
    QQApi.session.headers.update(
        {"referer": "http://m.y.qq.com", "User-Agent": config.get("ios_useragent")}
    )
    res_data = (
        QQApi.request(
            "http://c.y.qq.com/soso/fcgi-bin/search_for_qq_cp",
            method="GET",
            data=params,
        )
        .get("data", {})
        .get("song", {})
        .get("list", [])
    )


    for item in res_data:
        # 获得歌手名字
        if (item["msgid"]==13):
            continue
        singers = [s.get("name", "") for s in item.get("singer", "")]
        song = QQSong()
        song.source = "qq"
        song.id = item.get("songmid", "")
        song.title = item.get("songname", "")
        song.singer = "、".join(singers)
        song.album = item.get("albumname", "")
        song.duration = int(item.get("interval", 0))*1000
        song.size = round(item.get("size128", 0) / 1048576, 2)
        songs_list.append(song)
        if isinstance(allSong, queue.Queue):
            allSong.put(song.getObj())
    return songs_list
def getMp3Url(mid) ->str:
    # 计算vkey
    guid = str(random.randrange(1000000000, 10000000000))
    params = {
        "guid": guid,
        "loginUin": "3051522991",
        "format": "json",
        "platform": "yqq",
        "cid": "205361747",
        "uin": "3051522991",
        "songmid":mid,
        "needNewCode": 0,
    }
    rate_list = [
        # ("A000", "ape", 800),
        # ("F000", "flac", 800),
        ("M800", "mp3", 320),
        ("C400", "m4a", 128),
        ("M500", "mp3", 128),
    ]
    QQApi.session.headers.update({"referer": "http://y.qq.com"})
    for rate in rate_list:
        params["filename"] = "%s%s.%s" % (rate[0], mid, rate[1])
        url = "https://u.y.qq.com/cgi-bin/musicu.fcg?data=" \
              "{\"req_0\":{\"module\":\"vkey.GetVkeyServer\"," \
              "\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\""+params["guid"]+"\"," \
              "\"loginflag\":1,\"filename\":[\"" + params["filename"] + "\"],\"songmid\":[\"" + params["songmid"] + "\"],\"songtype\":[0],\"uin\":\"0\",\"platform\":\"20\"}}}"
        vkeys = requests.get(url).json().get("req_0",{}).get("data",{}).get("midurlinfo",[])
        if len(vkeys):
            if len(vkeys[0].get("purl",""))>50:
                songUrl = "http://dl.stream.qqmusic.qq.com/" + vkeys[0].get("purl","")
                return songUrl
    return ""
def qq_playlist(open_url):
    find_win=re.compile("id=(\d+)")
    playlist=re.findall(find_win,open_url)[0]##='7674449134'   ##指定歌单
    url='https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&disstid={}\
    &format=jsonp&g_tk=1547190586&jsonpCallback=playlistinfoCallback&loginUin=2020197426\
    &hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0'.format(playlist)
    header={}
    header['user-agent']='Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36'
    header['referer']='https://y.qq.com/n/yqq/playlist/{}.html'.format(playlist)

    req=requests.get(url,headers=header)

    req1=str(req.text).replace('playlistinfoCallback','').strip('()')#原text不能直接json,要把头跟末尾去掉才行！
    info=json.loads(req1)
    gedan={
        "name":"",
        "songlist":""
    }
    gedan["name"]=info['cdlist'][0]["dissname"]
    songinfo=info['cdlist'][0]['songlist']

    songs_list=[]
    songs_vip_list=[]
    "msgid==13为vip"
    for item in songinfo:
        song = QQSong()
        singers = [s.get("name", "") for s in item.get("singer", "")]
        if (item["msgid"]==13):#这是vip
            song.source = "qq"
            song.id = ""
            song.title = item.get("songname", "")
            song.singer = "、".join(singers)
            song.album = item.get("albumname", "")
            song.duration = int(item.get("interval", 0)) * 1000
            song.size = round(item.get("size128", 0) / 1048576, 2)
            songs_vip_list.append(song.getObj())
            continue
        song.source = "qq"
        song.id = item.get("songmid", "")
        song.title = item.get("songname", "")
        song.singer = "、".join(singers)
        song.album = item.get("albumname", "")
        song.duration = int(item.get("interval", 0)) * 1000
        song.size = round(item.get("size128", 0) / 1048576, 2)
        songs_list.append(song.getObj())
    gedan["songlist"]=songs_vip_list+songs_list
    return gedan



search = qq_search
playlist = qq_playlist


if __name__ == '__main__':
    print(playlist("https://i.y.qq.com/n2/m/share/details/taoge.html?platform=11&appshare=android_qq&appversion=10050006&hosteuin=oKn5NeEl7io57z**&id=7743481315&ADTAG=qfshare"))
