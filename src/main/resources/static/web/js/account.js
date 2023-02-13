let sidebar = document.querySelector('.sidebar');
let am_pm = document.querySelector('.am_pm');
let body = document.querySelector('.content');

am_pm.addEventListener('change',function(){
    if(am_pm.checked)
    body.classList.toggle('image_body');
    else
    body.classList.remove('image_body');
})

window.addEventListener('resize', function() {
    if (window.innerWidth < 800) {
        sidebar.style.display = 'none';
    } else {
        sidebar.style.display = 'block';
    }
});

//app the vue
const { createApp } = Vue;

createApp({
    data() {
        return {
            data: [],
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
        }
    }
}).mount('#app');