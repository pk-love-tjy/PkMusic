import json

from MyMusic.private import  kuwo, netease, kugou, qq,migu
import threading
import queue

pings = [
    kugou.search,
    kuwo.search,
    # netease.search,
    qq.search,
    migu.search
]
class MyMusicApi:
    def search(self,keyword):
        th=[]
        allSongQueue=queue.Queue()
        allSongList=[]
        for ping in pings:
            thread = threading.Thread(target=ping,args=(keyword,allSongQueue,))
            th.append(thread)
        for i in th:
            i.start()
        for i in th:
            i.join()
        for i in range(allSongQueue.qsize()):
            allSongList.append(allSongQueue.get())
        return allSongList
    def getUrl(self, id, source):
        if (source== "baidu"):
            pass
        elif (source == "kugou"):
            return kugou.getMp3Url(id)
        elif (source == "kuwo"):
            return kuwo.getMp3Url(id)
        elif (source == "netease"):
            return netease.getMp3Url(id)
        elif (source == "qq"):
            return qq.getMp3Url(id)
        elif (source == "migu"):
            return migu.getMp3Url(id)
        else:
            return ""
    def playlist(self,keyword,source):
        if (source== "baidu"):
            pass
        elif (source == "kugou"):
            pass
        elif (source == "kuwo"):
            pass
        elif (source == "netease"):
            pass
        elif (source == "qq"):
            return qq.playlist(keyword)
        elif (source == "migu"):
            pass
        else:
            return ""

def search(keyword):
    return json.dumps(MyMusicApi().search(keyword))

print(search("广寒宫"))


