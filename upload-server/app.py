import os
from flask import Flask, request, url_for, send_from_directory, json
from werkzeug.utils import secure_filename

app = Flask(__name__)
upload_dir = os.path.join(os.getcwd(), "Upload")


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/upload_file/<filename>')
def upload_file(filename):
    return send_from_directory(upload_dir, filename)


@app.route('/upload', methods=['GET', 'POST'])
def upload():
    if request.method == 'POST':
        file = request.files['file']
        filename = secure_filename(file.filename)
        file.save(os.path.join(upload_dir, filename))
        file_url = url_for('upload_file', filename=filename)
        return json.dumps({
            'code': 200,
            'msg': "成功",
            'data': {
                filename: 'http://127.0.0.1:5000' + file_url
            }
        })
    return ""


@app.route('/upload_new', methods=['POST'])
def upload_new():
    if request.headers['Content-Type'] == 'application/octet-stream':
        filename = '123.gif'
        with open(os.path.join(upload_dir, filename), 'wb+') as f:
            f.write(request.data)
        file_url = url_for('upload_file', filename=filename)
        return json.dumps({
            'code': 200,
            'msg': "成功",
            'data': {
                filename: 'http://127.0.0.1:5000' + file_url
            }
        })
    return ""


# 判断文件路径是否存在，不存在是否创建
def is_dir_existed(path, mkdir=True):
    if mkdir:
        if not os.path.exists(path):
            os.makedirs(path)
    else:
        return os.path.exists(path)


if __name__ == '__main__':
    is_dir_existed(upload_dir)
    app.run()
