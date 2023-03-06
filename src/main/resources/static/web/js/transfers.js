const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            navbar: true,
            title: true,
            numAccountDestini:"",
            numAccountOrigin:"",
            description:"",
            amount:"",
            show:"",
            accounts:[],
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        loadData: function () {
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data;
                    this.data.accounts.sort((a, b) => a.id - b.id);
                })
                .catch(err => console.log(err));
        },
        logout: function () {
            axios.post('/api/logout')
                .then(response => {
                    window.location.href = '/web/index.html';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
        },
        updateScreenSize: function () {
            this.title = window.innerWidth < 500;
            this.navbar = window.innerWidth < 750;
        },
        beforeDestroy() {
            window.removeEventListener("resize", this.updateScreenSize);
        },
        newTransfer:function(){
            axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&numAccountOrigin=${this.numAccountOrigin}&numAccountDestini=${this.numAccountDestini}`)
            .then(response=>{
                Swal.fire('transferencia con exito')
            })
            .catch(error=>{
                console.log(error);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data,
                })
            })
        },
        filterAccounts:function(){
            this.accounts=this.data.accounts.filter(account=>account.number!=this.numAccountOrigin);
        }
    },
    mounted(){
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');
/*Una campo para elegir si es una transferencia a cuenta propia o de tercero
En caso de ser propia debe haber un campo para seleccionar la cuenta de destino, que no puede ser la misma cuenta de origen.
En caso de ser a terceros debe haber un campo para ingresar el número de cuenta
Un campo para indicar el monto a transferir
Un campo para indicar la descripción

Al pulsar el botón transfer debe aparecer un cuadro de diálogo preguntando si está seguro de realizar la transferencia, si el usuario indica que sí entonces se realiza la petición de tipo POST al servicio de creación de transferencia, de lo contrario se cierra el modal. Si ocurre un error al realizar la transferencia se debe indicar al cliente, de lo contrario mostrar un mensaje indicando que la transferencia fue realizada correctamente y recargar la página.


Para obtener las cuentas del cliente al cargar la página puedes acceder al recurso en la ruta /clients/current/accounts, que se solicitó crear en el mail con el requerimiento para implementar la creación de cuentas.

Una buena idea es mostrar de alguna manera el saldo de la cuenta de origen seleccionada.*/