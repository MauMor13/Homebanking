//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: "",
            loans:"",
            navbar:false,
            title:true,
            id:0,
            amount:0,
            payments:"",
            numberAccountDestini:"",
            paymentsFilter:""
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
                })
                .catch(err => console.log(err));
        },
        loansExist:function(){
            axios.get("/api/loans")
            .then(response=>{
                console.log(response.data);
                this.loans=response.data;
                this.paymentsFilter=this.filterLoans();
            }).catch(err=>console.log(err));
        },
        filterLoans:function(){
            return this.loans.filter(loan=>loan.id==this.id)[0]?.payments;
        },
        newLoan:function(){
            axios.post("/api/loans",{"id":this.id,"amount":this.amount,"payments":this.payments,"numberAccountDestini":this.numberAccountDestini})
        .then(response=>{
            Swal.fire('Successful Loan Application')
        }).catch(err=>{
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