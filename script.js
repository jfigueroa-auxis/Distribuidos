const { from } = require('rxjs');
const { PubSub } = require('@google-cloud/pubsub');
const { Observable } = require('rxjs');
const { bufferTime, map, tap, filter } = require('rxjs/operators');

const pubSubClient = new PubSub();

const procesar = () => {

    const susbcripcion = pubSubClient.subscription('receptor');
    const mensajes = new Observable(sub => {
        susbcripcion.on('message', (msg) => {
            sub.next(JSON.parse(msg.data.toString()));
            msg.ack();
        })
    })

    mensajes.pipe(
        //tap(info => console.log(JSON.stringify(info)))
        bufferTime(300),
        filter(arr => arr.length > 0),
        map(arr => Array.from(new Set(arr.map(c => c.nombre))).map(nombre => arr.find(e => e.nombre == nombre))),
        map(arr => {
            let coords = arr.reduce((recolector, mobil) => {
                recolector.push({
                    geometry: {
                        coordinates: [mobil.longitud, mobil.latitud],
                        type: 'Point'
                    },
                    type: 'Feature',
                    properties: {
                        nombre: mobil.nombre,
                        aceleracion: mobil.aceleracion
                    }
                });
                return recolector;
            }, [])

            return {
                type: 'FeatureCollection',
                features: coords
            };
        })
        ,tap(info => console.log(JSON.stringify(info), new Date()))
    ).subscribe(dat => {
        from(pubSubClient.topic('coords-mapa').publishJSON(dat))
            .subscribe(
                () => { },
                (err) => console.log(err)
            )
    })
}

procesar();

