//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: "",
            loans:[],
            navbar:false,
            title:true,
            id:0,
            loanName:"Automotive",
            amount:"",
            payments:0,
            numberAccountDestini:"",
            paymentsFilter:"",
            avatarImg:1
        }
    },
    created() {
        this.avatarImg=localStorage.getItem("myAvatar")?localStorage.getItem("myAvatar"):1;
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
                this.loans=response.data;
                this.paymentsFilter=this.filterLoans();
            }).catch(err=>console.log(err));
        },
        filterLoans:function(){
            let loan = this.loans.filter(loan=>loan.id==this.id)[0];
            if(this.id>0)
                this.loanName = loan?.name;
            else
                this.loanName = 0

            return loan?.payments;
        },
        loanfiltrado:function(){
            if(this.id>0)
                return this.loans.filter(loan=>loan.id==this.id)?.[0];
            else
                return [];
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
        logout: function () {
            Swal.fire({
                title: 'Are you sure to go out?',
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout')
                        .then(response => {
                            Swal.fire('Logout successful!', '', 'success')
                            window.location.href = '/web/index.html';
                        })
                        .catch(error => {
                            this.error = error.response.data;
                        });
                }
            })
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