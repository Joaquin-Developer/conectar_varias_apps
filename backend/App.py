# -*- coding: utf-8 -*-

from flask import Flask, render_template, request, json, send_from_directory, jsonify, Response

PERSONS_LIST = []
# structure example:
# {
#     'id': 1,
#     'name': 'pepe'
# }

app = Flask(__name__)

@app.route("/", methods = ["GET"])
def index():
    return render_template("index.html")

@app.route("/api/v1/get_persons", methods=["GET"])
def get_data():
    return json.dumps(PERSONS_LIST, ensure_ascii= False)

@app.route("/api/v1/insert_persons", methods = ["POST"])
def insert_data():
    data = request.get_json(force=True)
    PERSONS_LIST.append({ "id": (len(PERSONS_LIST) + 1), "name": data.get("name") })
    message = "Datos insertados: id: {} Nombre: {}".format((len(PERSONS_LIST) + 1), data.get("name"))
    print(message)
    return json.dumps({ "data": message }, ensure_ascii= False)

# return css static files:
@app.route('/public/<path:path>')
def send_css(path):
    return send_from_directory('public', path)

@app.errorhandler(404)
def page_not_found(error):
    # return html file "Error 404: This file doesnt exists"
    return render_template("404errorPage.html")

def api_test():
    PERSONS_LIST.append({ "id": 1, "name": "Joaquín" })
    PERSONS_LIST.append({ "id": 2, "name": "Ana" })
    app.run(debug=True)

def run():
    app.run(debug=False)

if __name__ == '__main__':
    api_test()
