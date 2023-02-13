const { createApp } = Vue;

createApp({
    data() {
        return {
            account:[],
            data: [],
            navbar:true,
            title:true,
        }
    },
    created(){
        this.loadData();
        this.loadTransactions();
    },
    methods:{
        loadTransactions:function(){
            let stringUrl = location.search;//lee la url actual para seccion mas detalles
            let parameter= new URLSearchParams(stringUrl); 
            let id = parameter.get("id");
            axios.get('http://localhost:8080/api/accounts/'+id)
            .then(response=>{
                this.account=response.data;
                this.account.transaction.sort((a,b)=>a.id-b.id);
            })
            .catch(err=>console.log(err)); 
        },
            loadData: function () {
                axios.get("http://localhost:8080/api/clients/1")
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