const { createApp } = Vue;

createApp({
    data() {
        return {
            email: '',
            password: '',
            error: '',
            errorNumber: '',
            firstName: '',
            lastName: '',
            form: false,
            title: true,
        }
    },
    methods: {
        login: function () {
            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(response => {
                    if (this.email == "admin@mindhub.com") {
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Login successful!',
                            showConfirmButton: false,
                            timer: 1500
                        })
                        window.location.href = '/manager/manager.html';
                    }
                    else {
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Login successful!',
                            showConfirmButton: false,
                            timer: 1500
                        })
                        window.location.href = '/web/accounts.html';
                    }
                    this.email = '';
                    this.password = '';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                    this.email = '';
                    this.password = '';
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'I entered a wrong parameter',
                    })
                });
        },
        register: function () {
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
                .then(response => {
                    this.login();
                    this.email = '';
                    this.password = '';
                    this.error = '';
                    this.errorNumber = '';
                    this.firstName = '';
                    this.lastName = '';
                })
                .catch(error => {
                    console.log(error)
                    this.error = error.response.data.message;
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                    })
                });
        },
        loginRegister: function (value) {
            this.form = true;
            let form = document.querySelector('.card-3d-wrapper');
            if (value == 'register') {
                form.classList.add('girarLogin');
            }
            else if (value == 'login') {
                form.classList.remove('girarLogin');
            }
        },
        beforeDestroy: function () {
            window.removeEventListener("resize", this.updateScreenSize);
        },
        updateScreenSize: function () {
            this.title = window.innerWidth > 500;
        },
    },
    mounted() {
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
        const icons = [
            document.getElementById('icon1'),
            document.getElementById('icon2'),
            document.getElementById('icon3'),
            document.getElementById('icon4'),
            document.getElementById('icon5'),
            document.getElementById('icon6'),
            document.getElementById('icon7'),
            document.getElementById('icon8'),
            document.getElementById('icon9'),
            document.getElementById('icon10'),
            document.getElementById('icon11'),
            document.getElementById('icon12'),
            document.getElementById('icon13'),
            document.getElementById('icon14'),
            document.getElementById('icon15')];
        setInterval(() => {
            let random;
            for (let i = 0; i < 15; i++) {
                random = Math.round(Math.random());
                if (random == 0) {
                    icons[i].classList.remove('vibra');
                }
                else {
                    icons[i].classList.add('vibra');
                }
            }
        }, 500);
    },
}).mount('#app')

