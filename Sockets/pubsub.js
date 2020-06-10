const { from } = require('rxjs');
const { PubSub } = require('@google-cloud/pubsub');
const { Observable } = require('rxjs');
const { tap } = require('rxjs/operators');


const pubSubClient = new PubSub();

const entrada = (io) => {
    const canalMobil = io.of('/mobil')
    canalMobil.on('connection', socket => {
        socket.on('moving', datos => {
            from(pubSubClient.topic('transmisores').publishJSON(datos))
                .subscribe(
                    () => {},
                    (err) => console.log(err)
                )
        });
    });
}

const salida = (io) => {

    const susbcripcion = pubSubClient.subscription('mapa1');
    const mensajes = new Observable(sub => {
        susbcripcion.on('message', (msg) => {
            sub.next(JSON.parse(msg.data.toString()));
            msg.ack();
        })
    })

    mensajes.subscribe(dat => io.sockets.emit('process', JSON.stringify(dat)))
}

module.exports = { entrada, salida};