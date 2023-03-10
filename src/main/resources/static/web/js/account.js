const { createApp } = Vue;

createApp({
    data() {
        return {
            account: [],
            data: [],
            navbar: false,
            title: true,
            id: "",
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
                    this.data.accounts.sort((a, b) => a.id - b.id);
                    console.log(this.id)
                    let account = this.data.accounts.filter(account => account.id == this.id);
                    console.log(account[0].id)
                    this.account = account[0];
                })
                .catch(err => console.log(err));
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