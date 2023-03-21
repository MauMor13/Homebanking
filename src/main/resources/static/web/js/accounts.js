//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            navbar: false,
            title: true,
            accountType: "",
            accounts: [],
            numAccounts: 0,
            numLoans: 0,
            avatarImg: 1
        }
    },
    created() {
        this.avatarImg = localStorage.getItem("myAvatar") ? localStorage.getItem("myAvatar") : 1;
        this.loadData();
        this.loadAccounts();
    },
    methods: {
        loadData: function () {
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data;
                    this.numLoans = this.data.loans.length;
                })
                .catch(err => console.log(err));
        },
        loadAccounts: function () {
            axios.get("/api/clients/current/accounts")
                .then(response => {
                    this.accounts = response.data;
                    this.numAccounts = this.accounts.length;
                    this.accounts.sort((a, b) => a.id - b.id);
                })
                .catch(err => console.log(err));
        },
        deleteAccount: function (number) {
            Swal.fire({
                position: 'top-center',
                title: 'Are you sure to delete the account?',
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch("/api/account-delete", `numberAccount=${number}`)
                        .then(response => {
                            this.loadAccounts();
                            Swal.fire('Account Delete Successfully');
                        })
                        .catch(error => {
                            console.log(error.data);
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                            })
                        });
                }
            })
        },
        newAccount: function () {
            Swal.fire({
                position: 'center',
                title: 'Are you sure to create a new account?',
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
                .then(response => {
                    Swal.fire('Account Created Successfully');
                    this.loadAccounts();
                })
                .catch(error => {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                    })
                });
                }
            })
        },
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