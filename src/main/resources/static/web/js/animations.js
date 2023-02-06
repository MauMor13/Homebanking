//animacion de fondo 
// modified version of random-normal
function normalPool(o) { var r = 0; do { var a = Math.round(normal({ mean: o.mean, dev: o.dev })); if (a < o.pool.length && a >= 0) return o.pool[a]; r++ } while (r < 100) } function randomNormal(o) { if (o = Object.assign({ mean: 0, dev: 1, pool: [] }, o), Array.isArray(o.pool) && o.pool.length > 0) return normalPool(o); var r, a, n, e, l = o.mean, t = o.dev; do { r = (a = 2 * Math.random() - 1) * a + (n = 2 * Math.random() - 1) * n } while (r >= 1); return e = a * Math.sqrt(-2 * Math.log(r) / r), t * e + l }

const NUM_PARTICLES = 600;
const PARTICLE_SIZE = 0.5; // View heights
const SPEED = 20000; // Milliseconds

let particles = [];

function rand(low, high) {
    return Math.random() * (high - low) + low;
}

function createParticle(canvas) {
    const colour = {
        r: 45,
        g: randomNormal({ mean: 205, dev: 20 }),
        b: 223,
        a: rand(0, 1),
    };
    return {
        x: -2,
        y: -2,
        diameter: Math.max(0, randomNormal({ mean: PARTICLE_SIZE, dev: PARTICLE_SIZE / 2 })),
        duration: randomNormal({ mean: SPEED, dev: SPEED * 0.1 }),
        amplitude: randomNormal({ mean: 16, dev: 2 }),
        offsetY: randomNormal({ mean: 0, dev: 10 }),
        arc: Math.PI * 2,
        startTime: performance.now() - rand(0, SPEED),
        colour: `rgba(${colour.r}, ${colour.g}, ${colour.b}, ${colour.a})`,
        shape:"diamond",
    }
}

function moveParticle(particle, canvas, time) {
    const progress = ((time - particle.startTime) % particle.duration) / particle.duration;
    return {
        ...particle,
        x: progress,
        y: ((Math.sin(progress * particle.arc) * particle.amplitude) + particle.offsetY),
    };
}

function drawParticle(particle, canvas, ctx) {
    const vh = canvas.height / 100;
    const size = particle.diameter * vh * 2;
    const x = particle.x * canvas.width;
    const y = particle.y * vh + (canvas.height / 2);

    ctx.strokeStyle = particle.colour;
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(x, y - size/2);
    ctx.lineTo(x + size/4, y - size/4);
    ctx.lineTo(x + size/2, y);
    ctx.lineTo(x + size/4, y + size/4);
    ctx.lineTo(x, y + size/2);
    ctx.lineTo(x - size/4, y + size/4);
    ctx.lineTo(x - size/2, y);
    ctx.lineTo(x - size/4, y - size/4);
    ctx.closePath();
    ctx.stroke();
}

function draw(time, canvas, ctx) {
    // Move particles
    particles.forEach((particle, index) => {
        particles[index] = moveParticle(particle, canvas, time);
    })

    // Clear the canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw the particles
    particles.forEach((particle) => {
        drawParticle(particle, canvas, ctx);
    })

    // Schedule next frame
    requestAnimationFrame((time) => draw(time, canvas, ctx));
}

function initializeCanvas() {
    let canvas = document.getElementById('particle-canvas');
    canvas.width = canvas.offsetWidth * window.devicePixelRatio;
    canvas.height = canvas.offsetHeight * window.devicePixelRatio;
    let ctx = canvas.getContext("2d");

    window.addEventListener('resize', () => {
        canvas.width = canvas.offsetWidth * window.devicePixelRatio;
        canvas.height = canvas.offsetHeight * window.devicePixelRatio;
        ctx = canvas.getContext("2d");
    })

    return [canvas, ctx];
}

function startAnimation() {
    const [canvas, ctx] = initializeCanvas();

    // Create a bunch of particles
    for (let i = 0; i < NUM_PARTICLES; i++) {
        particles.push(createParticle(canvas));
    }

    requestAnimationFrame((time) => draw(time, canvas, ctx));
};

// Start animation when document is loaded
(function () {
    if (document.readystate !== 'loading') {
        startAnimation();
    } else {
        document.addEventListener('DOMContentLoaded', () => {
            startAnimation();
        })
    }
}());






