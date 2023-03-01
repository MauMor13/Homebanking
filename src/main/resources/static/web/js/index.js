const { createApp } = Vue

createApp({
    data() {
        return {
            email: '',
            password: '',
            error: '',
            errorNumber:'',
            firstName: '',
            lastName: '',
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
                .then(response => {
                    this.email= '';
                    this.password= '';
                    this.error= '';
                    this.firstName= '';
                    this.lastName= '';
                    window.location.href = '/web/index.html';
                })
                .catch(error => {
                    console.log(error);
                    this.error = error.response.data.message;
                });
        },
    },
}).mount('#app')

