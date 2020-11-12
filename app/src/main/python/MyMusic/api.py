#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
@author: HJK
@file: api.py
@time: 2019-06-11
"""

import requests
from MyMusic import config
from MyMusic.exceptions import RequestError, ResponseError


class MusicApi:
    # class property
    # 子类修改时使用deepcopy
    session = requests.Session()
    session.headers.update(config.get("fake_headers"))
    if config.get("proxies"):
        session.proxies.update(config.get("proxies"))
    session.headers.update({"referer": "http://www.google.com/"})

    @classmethod
    def request(cls, url, method="POST", data=None):
        if method == "GET":
            resp = cls.session.get(url, params=data, timeout=config.get("timeout"))
        else:
            resp = cls.session.post(url, data=data, timeout=config.get("timeout"))
        if resp.status_code != requests.codes.ok:
            raise RequestError(resp.text)
        if not resp.text:
            raise ResponseError("No response data.")
        return resp.json()
