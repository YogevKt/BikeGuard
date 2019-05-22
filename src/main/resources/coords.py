import pyproj
from flask import Flask, jsonify, request
import json
import requests

app = Flask(__name__)

# Definition of the function to convert Cartesian coordinates to Geodetic
@app.route('/cartesian_to_geodetic', methods = ['POST'])
def cartesian_to_geodetic():
    data = json.loads(request.data)
    x=data['x']
    y=data['y']
    z=data['z']
    ecef = pyproj.Proj(proj='geocent', ellps='WGS84', datum='WGS84')
    lla = pyproj.Proj(proj='latlong', ellps='WGS84', datum='WGS84')

    lon, lat, alt = pyproj.transform(ecef, lla, x, y, z, radians=False)

    print("\n")
    print ("Latitude: %f" % lat)
    print ("Longitude: %f" % lon)
    print ("Height in metres: %f" % alt)
    return jsonify({"Latitude":lat,"Longitude":lon,"Altitude":alt})


# Definition of the function to convert Geodetic coordinates to Cartesian
@app.route('/geodetic_to_cartesian', methods = ['POST'])
def geodetic_to_cartesian():
    data = json.loads(request.data)
    lat = data['Latitude']
    lon = data['Longitude']
    alt = data['Altitude']
    ecef = pyproj.Proj(proj='geocent', ellps='WGS84', datum='WGS84')
    lla = pyproj.Proj(proj='latlong', ellps='WGS84', datum='WGS84')

    x, y, z = pyproj.transform(lla, ecef, lon, lat, alt, radians=False)

    print("\n")
    print ("X: %f" % x)
    print ("Y: %f" % y)
    print ("Z: %f" % z)
    return jsonify({"x": x,"y":y,"z":z})

app.run(host='127.0.0.1',port=5000)