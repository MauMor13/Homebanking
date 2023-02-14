//app the vue
const { createApp } = Vue;

createApp({
    data() {
        return {
            data: [],
            navbar:true,
            title:true,
        }
    },
    created() {
        this.loadData();
        
    },
    methods: {
        loadData: function () {
            axios.get("http://localhost:8080/api/clients/1")
                .then(response => {
                    this.data = response.data;
                    this.data.accounts.sort((a, b) => a.id - b.id);
                    console.log(this.data);
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
        updateScreenSize:function() {
            this.title = window.innerWidth < 500;
            this.navbar = window.innerWidth < 750;
        },
        changeBackground:function(events){
            if(events.target.checked){
            document.body.classList.remove('image_day');
            document.body.classList.toggle('image_night');
            }
            else{
            document.body.classList.remove('image_night');
            document.body.classList.toggle('image_day');
        }},
    },
    mounted(){
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
        beforeDestroy() {
        window.removeEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');