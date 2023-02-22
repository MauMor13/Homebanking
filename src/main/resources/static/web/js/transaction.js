const { createApp } = Vue;

createApp({
    data() {
        return {
            account:[],
            data: [],
            navbar:true,
            title:true,
            id:"",
        }
    },
    created(){
        let stringUrl = location.search;//lee la url actual para seccion mas detalles
        let parameter= new URLSearchParams(stringUrl); 
        this.id = parameter.get("id");
        this.loadData();
        this.loadTransactions();
    },
    methods:{
        loadTransactions:function(){
            axios.get('/api/accounts/' + this.id)
            .then(response=>{
                this.account=response.data;
                console.log(this.account.transaction)
                this.account.transaction.sort((a,b)=>a.id-b.id);
            })
            .catch(err=>console.log(err)); 
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
        loadData: function () {
                axios.get('/api/clients/current')
                    .then(response => {
                        this.data = response.data;
                        this.data.accounts.sort((a, b) => a.id - b.id);
                    })
                    .catch(err => console.log(err));
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