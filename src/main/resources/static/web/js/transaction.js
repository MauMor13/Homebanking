const { createApp } = Vue;

createApp({
    data() {
        return {
            account:[],
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData:function(){
            let stringUrl = location.search;//lee la url actual para seccion mas detalles
            let parameter= new URLSearchParams(stringUrl); 
            let id = parameter.get("id");
            console.log(id);
            axios.get('http://localhost:8080/api/accounts/'+id)
            .then(response=>{
                console.log(response.data);
                this.account=response.data;
                this.account.transaction.sort((a,b)=>a.id-b.id);
                console.log(this.account);
            })
            .catch(err=>console.log(err));
        },
    }
}).mount('#app');