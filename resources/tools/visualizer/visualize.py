#!/usr/bin/env python
import sys
import os
import re
import webbrowser


def generate(gamedata, generated_path):
    template_path = os.path.join(os.path.dirname(__file__), 'index.html')
    template = open(template_path, 'r')
    content = template.read()
    template.close()
    php_re = re.compile(r"<!\-\-\sGAMEDATA\s\-\->", re.S)
    javascript = gamedata
    content = php_re.sub(javascript, content)
    output = open(generated_path, 'w')
    output.write(content)
    output.close()

if __name__ == "__main__":
    data = [line for line in sys.stdin.readlines() if line != "\n"]
    data = data[-1]

    generated_path = os.path.realpath(os.path.join(os.path.dirname(__file__), 'generated.html'))

    generate(data, generated_path)
    webbrowser.open('file://'+generated_path)
