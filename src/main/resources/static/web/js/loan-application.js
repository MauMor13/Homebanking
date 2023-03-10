//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: {},
            loans:{},
            navbar:false,
            title:true,
            id:0,
            amount:"",
            payments:0,
            numberAccountDestini:"",
        }
    },
    created() {
        this.loadData();
        this.loansExist();
    },
    methods: {
        loadData: function () {
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data;
                    console.log(this.data);
                })
                .catch(err => console.log(err));
        },
        loansExist:function(){
            axios.get("/api/loans")
            .then(response=>{
                this.loans=response.data;
            }).catch(err=>console.log(err));
        },
        newLoan:function(){
            axios.post("/api/loans",{"id":this.id,"amount":this.amount,"payments":this.payments,"numberAccountDestini":this.numberAccountDestini})
        .then(response=>{
            Swal.fire('Successful Loan Application')
        }).catch(err=>{
            console.log(err);
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: err.response.data,
            })
        })
        },
        logout:function() {
            axios.post('/api/logout') 
                .then(response => {
                    window.location.href = '/web/index.html';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
        },
        navShow:function(value){
            this.navbar=value;
        },
        beforeDestroy:function() {
            window.removeEventListener("resize", this.updateScreenSize);
        },
        updateScreenSize: function () {
            this.title = window.innerWidth > 500;
        },
    },
    mounted(){
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');