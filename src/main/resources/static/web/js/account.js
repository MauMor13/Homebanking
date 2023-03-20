const { createApp } = Vue;

createApp({
    data() {
        return {
            data: [],
            transactions: [],
            navbar: false,
            title: true,
            id: "",
            fromAccount: "VIN-001",
            startDate: null,
            endDate: null,
            description: null,
            maxAmount: null,
            minAmount: null,
            type: null,
        }
    },
    created() {
        let stringUrl = location.search;//lee la url actual para seccion mas detalles
        let parameter = new URLSearchParams(stringUrl);
        this.id = parameter.get("id");
        this.loadData();
    },
    methods: {
        logout: function () {
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
                    this.fromAccount = this.data.accounts.filter(account => account.id == this.id)[0].number;
                    this.loadTransactions();
                })
                .catch(err => console.log(err));
        },
        loadTransactions: function () {
            axios.get('/api/filter-transactions', {
                params: {
                    fromAccount: this.fromAccount,
                    startDate: this.startDate,
                    endDate: this.endDate,
                    description: this.description,
                    maxAmount: this.maxAmount,
                    minAmount: this.minAmount,
                    type: this.type
                }
            }).then(response => {
                this.transactions = response.data;
            })
                .catch(err => console.log(err));
        },
        printPdf:function(){
            const elem=document.querySelector(".print-content");
            let opt = {
                margin:[15,0],
                filename:`Invoice-${("0000"+(Math.random()*9999)).slice(-4)}.pdf`,
            };
            html2pdf().set(opt).from(elem).save();
        },
        navShow: function (value) {
            this.navbar = value;
        },
        beforeDestroy: function () {
            window.removeEventListener("resize", this.updateScreenSize);
        },
        updateScreenSize: function () {
            this.title = window.innerWidth > 500;
        },
    },
    mounted() {
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');