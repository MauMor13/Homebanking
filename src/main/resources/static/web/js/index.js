const { createApp } = Vue;

createApp({
    data() {
        return {
            email: '',
            password: '',
            error: '',
            errorNumber:'',
            firstName: '',
            lastName: '',
            form:false,
        }
    },
    methods: {
        login:function() {
            axios.post('/api/login',`email=${this.email}&password=${this.password}`)
                .then(response => {
                    this.email= '';
                    this.password= '';
                    window.location.href = '/web/accounts.html';
                })
                .catch(error => {
                    console.log(error);
                    this.error = error.response.data.message;
                });
        },
        register:function() {
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
                .then(response => {this.login();
                    this.email= '';
                    this.password= '';
                    this.error= '';
                    this.errorNumber='';
                    this.firstName= '';
                    this.lastName= '';
                })
                .catch(error => {
                    console.log(error);
                    this.error = error.response.data.message;
                });
        },
        loginRegister:function(value){
            let form=document.querySelector('.card-3d-wrapper');
            if (value=='register'){
            this.form=true;
            form.classList.add('girarLogin');}
            else if(value=='login'){
            form.classList.remove('girarLogin');
            this.form=true;}
        },
    },
}).mount('#app')

