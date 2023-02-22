const { createApp } = Vue

createApp({
    data() {
        return {
            email: '',
            password: '',
            error: '',
            firstName: '',
            lastName: '',
        }
    },
    methods: {
        login:function() {
            axios.post('/api/login',`email=${this.email}&password=${this.password}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
                .then(response => {
                    window.location.href = '/web/accounts.html';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
        },
        register:function() {
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
                .then(response => {
                    window.location.href = '/web/index.html';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
        },
        cambiar_login:function() {
            document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_login";
            document.querySelector('.cont_form_login').style.display = "block";
            document.querySelector('.cont_form_sign_up').style.opacity = "0";
            setTimeout(function () { document.querySelector('.cont_form_login').style.opacity = "1"; }, 400);
            setTimeout(function () {
                document.querySelector('.cont_form_sign_up').style.display = "none";
            }, 200);
        },
        cambiar_sign_up:function(at) {
            document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_sign_up";
            document.querySelector('.cont_form_sign_up').style.display = "block";
            document.querySelector('.cont_form_login').style.opacity = "0";
            setTimeout(function () {
                document.querySelector('.cont_form_sign_up').style.opacity = "1";
            }, 100);
            setTimeout(function () {
                document.querySelector('.cont_form_login').style.display = "none";
            }, 400);
        },
        ocultar_login_sign_up:function() {
            document.querySelector('.cont_forms').className = "cont_forms";
            document.querySelector('.cont_form_sign_up').style.opacity = "0";
            document.querySelector('.cont_form_login').style.opacity = "0";
            setTimeout(function () {
                document.querySelector('.cont_form_sign_up').style.display = "none";
                document.querySelector('.cont_form_login').style.display = "none";
            }, 500);
        },
    }
}).mount('#app')

