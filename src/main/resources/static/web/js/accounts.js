//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            navbar:false,
            title:true,
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
        grapicAccount: function (account) {
            let options = {
                chart: {
                    type: 'line'
                },
                series: [{
                    name: 'sales',
                    data:account.transaction.map(transaction=>transaction.amount),
                }],
                xaxis: {
                    categories: account.transaction.map(transaction=>transaction.date.split("T")[0]),
                }
            }
            let elementId=document.getElementById("grapic" + account.id);
            if(!elementId) return;
            elementId.innerHTML="";
            let chart = new ApexCharts(elementId, options);
            chart.render();
        }, 
        newAccount:function() {
            axios.post('/api/clients/current/accounts') 
                .then(response => {
                    this.loadData();
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
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