const { createApp } = Vue;

createApp({
    data() {
        return {
            data: [],
            accountsActive:[],
            transactions: [],
            navbar: false,
            title: true,
            id: "",
            fromAccount: "",
            startDate: null,
            endDate: null,
            description: null,
            maxAmount: null,
            minAmount: null,
            type: null,
            avatarImg:1,
        }
    },
    created() {
        this.avatarImg=localStorage.getItem("myAvatar")?localStorage.getItem("myAvatar"):1;
        let stringUrl = location.search;//lee la url actual para seccion mas detalles
        let parameter = new URLSearchParams(stringUrl);
        this.id = parameter.get("id");
        this.loadData();
    },
    methods: {
        logout: function () {
            Swal.fire({
                position: 'center',
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
        loadData: function () {
            axios.get('/api/clients/current')
                .then(response => {
                    this.data = response.data;
                    this.fromAccount = this.data.accounts.filter(account => account.id == this.id)[0].number;
                    this.accountsActive = this.data.accounts.filter(account=> account.active);
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
            const element=document.querySelector(".print-content");
            let opt = {
                margin: 0.5,
                filename: 'TransactionsSummary.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 1 },
                jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
            };
            html2pdf().set(opt).from(element).save();
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