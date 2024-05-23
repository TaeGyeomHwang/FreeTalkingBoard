const deleteUser = document.getElementById("delete-user");

document.addEventListener("DOMContentLoaded", function () {
    const deleteUser = document.getElementById("delete-user");

    if (deleteUser) {
        deleteUser.addEventListener("click", (event) => {
            fetch("/deleteMember", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: document.getElementById("userEmails").value,
                    isDeleted: 1,
                }),
            }).then(() => {
                alert("사용자 정지 완료했습니다.");
                window.location.href = '/';
            });
        });
    }
});